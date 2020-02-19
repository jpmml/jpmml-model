/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.visitors;

public class AttributeInternerBattery extends VisitorBattery {

	public AttributeInternerBattery(){
		add(StringInterner.class);
		add(IntegerInterner.class);
		add(FloatInterner.class);
		add(DoubleInterner.class);
	}
}