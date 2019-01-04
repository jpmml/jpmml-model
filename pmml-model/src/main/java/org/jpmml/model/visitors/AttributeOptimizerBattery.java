/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.jpmml.model.VisitorBattery;

public class AttributeOptimizerBattery extends VisitorBattery {

	public AttributeOptimizerBattery(){
		add(NodeScoreParser.class);
	}
}