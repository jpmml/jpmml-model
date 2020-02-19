/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.visitors;

public class ListFinalizerBattery extends VisitorBattery {

	public ListFinalizerBattery(){
		add(RowCleaner.class);
		add(ArrayListTransformer.class);
		add(ArrayListTrimmer.class);
	}
}