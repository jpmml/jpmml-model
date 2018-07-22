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
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.annotations.Added;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Removed;
import org.jpmml.model.annotations.Required;

/**
 * <p>
 * A Visitor that determines the range of valid PMML schema versions for a class model object.
 * </p>
 *
 * @see Added
 * @see Optional
 * @see Removed
 * @see Required
 */
public class VersionInspector extends AbstractVisitor {

	private Version minimum = Version.getMinimum();

	private Version maximum = Version.getMaximum();


	@Override
	public void applyTo(Visitable visitable){
		this.minimum = Version.getMinimum();
		this.maximum = Version.getMaximum();

		super.applyTo(visitable);
	}

	@Override
	public VisitorAction visit(PMMLObject object){

		for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()){
			inspect(clazz);
		}

		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());
		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			inspect(field, value);

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

				inspect(enumField);
			}
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(Apply apply){
		String function = apply.getFunction();

		Version version = VersionInspector.functionVersions.get(function);
		if(version != null){
			updateMinimum(version);
		}

		return super.visit(apply);
	}

	private void inspect(Field field, Object value){
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
					updateMinimum(optional.value());
				}

				Required required = field.getAnnotation(Required.class);
				if(required != null){
					updateMaximum(previous(required.value()));
				}

				return;
			}
		}

		inspect(field);
	}

	private void inspect(AnnotatedElement element){
		Added added = element.getAnnotation(Added.class);
		if(added != null){
			updateMinimum(added.value());
		}

		Removed removed = element.getAnnotation(Removed.class);
		if(removed != null){
			updateMaximum(removed.value());
		}
	}

	/**
	 * <p>
	 * The minimum (ie. earliest) PMML schema version that can fully represent this class model object.
	 * </p>
	 *
	 * @see Version#getMinimum()
	 */
	public Version getMinimum(){
		return this.minimum;
	}

	private void updateMinimum(Version minimum){

		if(minimum != null && minimum.compareTo(this.minimum) > 0){
			this.minimum = minimum;
		}
	}

	/**
	 * <p>
	 * The maximum (ie. latest) PMML schema version that can fully represent this class model object.
	 * </p>
	 *
	 * @see Version#getMaximum()
	 */
	public Version getMaximum(){
		return this.maximum;
	}

	private void updateMaximum(Version maximum){

		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
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
			"+", "-", "*", "/",
			"min", "max", "sum", "avg",
			"log10", "ln", "sqrt", "abs",
			"uppercase", "substring", "trimBlanks",
			"formatNumber", "formatDatetime",
			"dateDaysSinceYear", "dateSecondsSinceYear", "dateSecondsSinceMidnight");
		declareFunctions(Version.PMML_3_1,
			"exp", "pow", "threshold", "floor", "ceil", "round");
		declareFunctions(Version.PMML_4_0,
			"isMissing", "isNotMissing",
			"equal", "notEqual", "lessThan", "lessOrEqual", "greaterThan", "greaterOrEqual",
			"and", "or",
			"not",
			"isIn", "isNotIn",
			"if");
		declareFunctions(Version.PMML_4_1,
			"median", "product",
			"lowercase");
		declareFunctions(Version.PMML_4_2,
			"concat", "replace", "matches");
		declareFunctions(Version.PMML_4_3,
			"normalCDF", "normalPDF", "stdNormalCDF", "stdNormalPDF", "erf", "normalIDF", "stdNormalIDF");
	}

	static
	private void declareFunctions(Version version, String... functions){

		for(String function : functions){
			VersionInspector.functionVersions.put(function, version);
		}
	}
}