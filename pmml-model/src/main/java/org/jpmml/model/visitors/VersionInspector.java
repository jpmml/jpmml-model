/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.Apply;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.schema.Added;
import org.jpmml.schema.Removed;
import org.jpmml.schema.Version;

/**
 * <p>
 * A Visitor that determines the range of valid PMML schema versions for a class model object.
 * </p>
 *
 * @see Added
 * @see Removed
 */
public class VersionInspector extends AbstractSimpleVisitor {

	private Version minimum = Version.getMinimum();

	private Version maximum = Version.getMaximum();


	@Override
	public VisitorAction visit(PMMLObject object){

		for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()){
			inspect(clazz);
		}

		List<Field> fields = ReflectionUtil.getAllInstanceFields(object);
		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			// The field is not set
			if(value == null){
				continue;
			}

			Class<?> type = field.getType();

			// The field is set, but the value represents a default value
			if(type.isPrimitive() && ReflectionUtil.isDefaultValue(value)){
				continue;
			}

			inspect(field);

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

		return VisitorAction.CONTINUE;
	}

	@Override
	public VisitorAction visit(Apply apply){
		inspect(apply.getFunction());

		return super.visit(apply);
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

	private void inspect(String function){
		Version version = VersionInspector.functionVersions.get(function);
		if(version != null){
			updateMinimum(version);
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
	}

	static
	private void declareFunctions(Version version, String... functions){

		for(String function : functions){
			VersionInspector.functionVersions.put(function, version);
		}
	}
}