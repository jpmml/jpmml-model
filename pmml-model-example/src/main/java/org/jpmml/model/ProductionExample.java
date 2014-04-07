/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.dmg.pmml.*;

import com.beust.jcommander.Parameter;

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