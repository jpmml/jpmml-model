/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.DataField;
import org.dmg.pmml.DerivedField;
import org.dmg.pmml.Field;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.ParameterField;
import org.dmg.pmml.ResultField;
import org.dmg.pmml.VisitorAction;

abstract
public class AbstractFieldVisitor extends AbstractVisitor {

	abstract
	public VisitorAction visit(Field field);

	@Override
	public VisitorAction visit(DataField dataField){
		return visit((Field)dataField);
	}

	@Override
	public VisitorAction visit(DerivedField derivedField){
		return visit((Field)derivedField);
	}

	@Override
	public VisitorAction visit(OutputField outputField){
		return visit((Field)outputField);
	}

	@Override
	public VisitorAction visit(ParameterField parameterField){
		return visit((Field)parameterField);
	}

	@Override
	public VisitorAction visit(ResultField resultField){
		return visit((Field)resultField);
	}
}