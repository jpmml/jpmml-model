/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.example;

import java.io.File;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.PMML;

abstract
public class ProductionExample extends Example {

	@Parameter (
		names = {"--output"},
		description = "Output PMML file",
		required = true
	)
	private File output = null;


	abstract
	public PMML produce();

	@Override
	public void execute() throws Exception {
		PMML pmml = produce();

		marshalPMML(pmml, this.output);
	}
}