/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.jpmml.model.VisitorBattery;

public class AttributeInternerBattery extends VisitorBattery {

	public AttributeInternerBattery(){
		add(StringInterner.class);
		add(IntegerInterner.class);
		add(FloatInterner.class);
		add(DoubleInterner.class);
	}
}