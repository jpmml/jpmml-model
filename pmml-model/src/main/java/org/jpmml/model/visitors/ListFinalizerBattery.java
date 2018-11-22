/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.jpmml.model.VisitorBattery;

public class ListFinalizerBattery extends VisitorBattery {

	public ListFinalizerBattery(){
		add(RowCleaner.class);
		add(ArrayListTransformer.class);
		add(ArrayListTrimmer.class);
	}
}