/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.PMML;
import org.xml.sax.InputSource;

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
		Unmarshaller unmarshaller = createUnmarshaller();

		PMML pmml;

		try(InputStream is = new FileInputStream(this.input)){
			Source source = ImportFilter.apply(new InputSource(is));

			pmml = (PMML)unmarshaller.unmarshal(source);
		}

		consume(pmml);
	}
}