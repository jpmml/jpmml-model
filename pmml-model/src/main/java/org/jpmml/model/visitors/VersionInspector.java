/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dmg.pmml.Apply;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.AbstractSimpleVisitor;
import org.jpmml.schema.Added;
import org.jpmml.schema.Removed;
import org.jpmml.schema.Version;

/**
 * A visitor that determines the range of valid PMML schema versions for a class model object.
 *
 * @see Added
 * @see Removed
 */
public class VersionInspector extends AbstractSimpleVisitor {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public VisitorAction visit(PMMLObject object){
		Class<?> clazz = object.getClass();
		inspect(clazz);

		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			Object value;

			try {
				if(!field.isAccessible()){
					field.setAccessible(true);
				}

				value = field.get(object);
			} catch(IllegalAccessException iae){
				throw new RuntimeException(iae);
			}

			// The field is not set
			if(value == null){
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
	 * The minimum (ie. earliest) supported PMML schema version.
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
	 * The maximum (ie. latest) supported PMML schema version.
	 */
	public Version getMaximum(){
		return this.maximum;
	}

	private void updateMaximum(Version maximum){

		if(maximum != null && maximum.compareTo(this.maximum) < 0){
			this.maximum = maximum;
		}
	}

	private static Map<String, Version> functionVersions = new LinkedHashMap<String, Version>();

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