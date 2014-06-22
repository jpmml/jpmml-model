/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

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

		OutputStream os = new FileOutputStream(this.output);

		try {
			Result result = new StreamResult(os);

			JAXBUtil.marshalPMML(pmml, result);
		} finally {
			os.close();
		}
	}
}