/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.Apply;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLFunctions;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;

/**
 * @see Added
 * @see Optional
 * @see Removed
 * @see Required
 */
abstract
public class VersionInspector extends AbstractVisitor {

	abstract
	public void updateMinimum(PMMLObject object, AnnotatedElement element, Version minimum);

	abstract
	public void updateMaximum(PMMLObject object, AnnotatedElement element, Version maximum);

	@Override
	public VisitorAction visit(PMMLObject object){

		for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()){
			inspect(object, clazz);
		}

		List<Field> fields = ReflectionUtil.getFields(object.getClass());
		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			inspect(object, field, value);

			// The field is set to an enum constant
			if(value instanceof Enum){
				Enum<?> enumValue = (Enum<?>)value;

				Field enumField;

				try {
					Class<?> enumClazz = enumValue.getClass();

					enumField = enumClazz.getField(enumValue.name());
				} catch(NoSuchFieldException nsfe){
					throw new RuntimeException(nsfe);
				}

				inspect(object, enumField);
			}
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(Apply apply){
		String function = apply.requireFunction();

		Version version = VersionInspector.functionVersions.get(function);
		if(version != null){
			updateMinimum(apply, PMMLAttributes.APPLY_FUNCTION, version);
		}

		return super.visit(apply);
	}

	private void inspect(PMMLObject object, Field field, Object value){
		Class<?> type = field.getType();

		if(type.isPrimitive()){

			if(ReflectionUtil.isDefaultValue(value)){
				return;
			}
		} else

		{
			if(isNull(value)){
				Optional optional = field.getAnnotation(Optional.class);
				if(optional != null){
					updateMinimum(object, field, optional.value());
				}

				Required required = field.getAnnotation(Required.class);
				if(required != null){
					updateMaximum(object, field, previous(required.value()));
				}

				return;
			}
		}

		inspect(object, field);
	}

	private void inspect(PMMLObject object, AnnotatedElement element){
		Added added = element.getAnnotation(Added.class);
		if(added != null){
			updateMinimum(object, element, added.value());
		}

		Removed removed = element.getAnnotation(Removed.class);
		if(removed != null){
			updateMaximum(object, element, removed.value());
		}
	}

	static
	private boolean isNull(Object value){

		if(value instanceof Collection){
			Collection<?> collection = (Collection<?>)value;

			return collection.isEmpty();
		}

		return (value == null);
	}

	static
	private Version previous(Version version){
		Version[] values = Version.values();

		return values[version.ordinal() - 1];
	}

	private static Map<String, Version> functionVersions = new LinkedHashMap<>();

	static {
		declareFunctions(Version.PMML_3_0,
			PMMLFunctions.ADD, PMMLFunctions.SUBTRACT, PMMLFunctions.MULTIPLY, PMMLFunctions.DIVIDE,
			PMMLFunctions.MIN, PMMLFunctions.MAX, PMMLFunctions.SUM, PMMLFunctions.AVG,
			PMMLFunctions.LOG10, PMMLFunctions.LN, PMMLFunctions.SQRT, PMMLFunctions.ABS,
			PMMLFunctions.UPPERCASE, PMMLFunctions.SUBSTRING, PMMLFunctions.TRIMBLANKS,
			PMMLFunctions.FORMATNUMBER, PMMLFunctions.FORMATDATETIME,
			PMMLFunctions.DATEDAYSSINCEYEAR, PMMLFunctions.DATESECONDSSINCEYEAR, PMMLFunctions.DATESECONDSSINCEMIDNIGHT);
		declareFunctions(Version.PMML_3_1,
			PMMLFunctions.EXP, PMMLFunctions.POW, PMMLFunctions.THRESHOLD, PMMLFunctions.FLOOR, PMMLFunctions.CEIL, PMMLFunctions.ROUND);
		declareFunctions(Version.PMML_4_0,
			PMMLFunctions.ISMISSING, PMMLFunctions.ISNOTMISSING,
			PMMLFunctions.EQUAL, PMMLFunctions.NOTEQUAL, PMMLFunctions.LESSTHAN, PMMLFunctions.LESSOREQUAL, PMMLFunctions.GREATERTHAN, PMMLFunctions.GREATEROREQUAL,
			PMMLFunctions.AND, PMMLFunctions.OR,
			PMMLFunctions.NOT,
			PMMLFunctions.ISIN, PMMLFunctions.ISNOTIN,
			PMMLFunctions.IF);
		declareFunctions(Version.PMML_4_1,
			PMMLFunctions.MEDIAN, PMMLFunctions.PRODUCT,
			PMMLFunctions.LOWERCASE);
		declareFunctions(Version.PMML_4_2,
			PMMLFunctions.CONCAT, PMMLFunctions.REPLACE, PMMLFunctions.MATCHES);
		declareFunctions(Version.PMML_4_3,
			PMMLFunctions.NORMALCDF, PMMLFunctions.NORMALPDF, PMMLFunctions.STDNORMALCDF, PMMLFunctions.STDNORMALPDF, PMMLFunctions.ERF, PMMLFunctions.NORMALIDF, PMMLFunctions.STDNORMALIDF);
		declareFunctions(Version.PMML_4_4,
			PMMLFunctions.MODULO,
			PMMLFunctions.ISVALID, PMMLFunctions.ISNOTVALID,
			PMMLFunctions.EXPM1, PMMLFunctions.HYPOT, PMMLFunctions.LN1P, PMMLFunctions.RINT,
			PMMLFunctions.STRINGLENGTH,
			PMMLFunctions.SIN, PMMLFunctions.ASIN, PMMLFunctions.SINH, PMMLFunctions.COS, PMMLFunctions.ACOS, PMMLFunctions.COSH, PMMLFunctions.TAN, PMMLFunctions.ATAN, PMMLFunctions.TANH);
		declareFunctions(Version.XPMML,
			PMMLFunctions.ATAN2);
	}

	static
	private void declareFunctions(Version version, String... functions){

		for(String function : functions){
			VersionInspector.functionVersions.put(function, version);
		}
	}
}