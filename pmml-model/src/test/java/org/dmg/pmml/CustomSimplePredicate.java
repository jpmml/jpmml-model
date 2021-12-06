/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.jpmml.model.ReflectionUtil;

@XmlRootElement (
	name = "SimplePredicate"
)
public class CustomSimplePredicate extends SimplePredicate {

	public CustomSimplePredicate(){
		super();
	}

	public CustomSimplePredicate(String name, Operator operator, String value){
		super(name, operator, value);
	}

	public CustomSimplePredicate(SimplePredicate simplePredicate){
		super();

		ReflectionUtil.copyState(simplePredicate, this);
	}
}