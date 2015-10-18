/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlRootElement;

import org.jpmml.model.ReflectionUtil;

@XmlRootElement (
	name = "SimplePredicate"
)
public class CustomSimplePredicate extends SimplePredicate {

	public CustomSimplePredicate(){
		super();
	}

	public CustomSimplePredicate(FieldName name, Operator operator, String value){
		super(name, operator);

		setValue(value);
	}

	public CustomSimplePredicate(SimplePredicate simplePredicate){
		super();

		ReflectionUtil.copyState(simplePredicate, this);
	}
}