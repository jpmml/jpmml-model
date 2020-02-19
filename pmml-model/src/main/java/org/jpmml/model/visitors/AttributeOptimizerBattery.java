/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

public class AttributeOptimizerBattery extends VisitorBattery {

	public AttributeOptimizerBattery(){
		add(NodeScoreOptimizer.class);
	}
}