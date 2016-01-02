/*
 * Copyright (c) 2011 University of Tartu
 */
package org.jpmml.model;

import java.io.File;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.PMML;

abstract
public class TransformationExample extends Example {

	@Parameter (
		names = {"--input"},
		description = "Input PMML file",
		required = true
	)
	private File input = null;

	@Parameter (
		names = {"--output"},
		description = "Output PMML file",
		required = true
	)
	private File output = null;


	abstract
	public PMML transform(PMML pmml) throws Exception;

	@Override
	public void execute() throws Exception {
		PMML pmml = unmarshalPMML(this.input);

		pmml = transform(pmml);

		marshalPMML(pmml, this.output);
	}
}