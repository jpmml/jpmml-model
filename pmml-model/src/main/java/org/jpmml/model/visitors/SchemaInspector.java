/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dmg.pmml.Apply;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.schema.Added;
import org.jpmml.schema.Removed;
import org.jpmml.schema.Version;

public class SchemaInspector extends AnnotationInspector {

	private Version minimum = Version.PMML_3_0;

	private Version maximum = Version.PMML_4_2;


	@Override
	public VisitorAction visit(PMMLObject object){
		VisitorAction result = super.visit(object);

		if(object instanceof Apply){
			Apply apply = (Apply)object;

			inspect(apply.getFunction());
		}

		return result;
	}

	@Override
	public void inspect(AnnotatedElement element){
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
		Version version = SchemaInspector.functionVersions.get(function);
		if(version != null){
			updateMinimum(version);
		}
	}

	public Version getMinimum(){
		return this.minimum;
	}

	private void updateMinimum(Version minimum){

		if(minimum != null && minimum.compareTo(this.minimum) > 0){
			this.minimum = minimum;
		}
	}

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
			SchemaInspector.functionVersions.put(function, version);
		}
	}
}