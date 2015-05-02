/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import com.beust.jcommander.Parameter;
import org.xml.sax.InputSource;

public class ValidationExample extends Example {

	@Parameter (
		names = {"--input"},
		description = "Input PMML file",
		required = true
	)
	private File input = null;


	static
	public void main(String... args) throws Exception {
		execute(ValidationExample.class, args);
	}

	@Override
	public void execute() throws Exception {
		Schema schema = JAXBUtil.getSchema();

		Unmarshaller unmarshaller = JAXBUtil.createUnmarshaller();
		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new SimpleValidationEventHandler());

		InputStream is = new FileInputStream(this.input);

		try {
			Source source = ImportFilter.apply(new InputSource(is));

			unmarshaller.unmarshal(source);
		} finally {
			is.close();
		}
	}

	public class SimpleValidationEventHandler implements ValidationEventHandler {

		@Override
		public boolean handleEvent(ValidationEvent event){
			Level level = (event.getSeverity() == ValidationEvent.WARNING ? Level.WARNING : Level.SEVERE);

			ValidationExample.logger.log(level, String.valueOf(event));

			return true;
		}
	}

	private static final Logger logger = Logger.getLogger(ValidationExample.class.getName());
}