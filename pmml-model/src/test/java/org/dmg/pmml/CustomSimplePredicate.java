/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlRootElement;

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
}