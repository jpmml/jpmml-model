/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.PMML;

abstract
public class ConsumptionExample extends Example {

	@Parameter (
		names = {"--input"},
		description = "Input PMML file",
		required = true
	)
	private File input = null;


	abstract
	public void consume(PMML pmml);

	@Override
	public void execute() throws Exception {
		PMML pmml = unmarshalPMML(this.input);

		consume(pmml);
	}
}