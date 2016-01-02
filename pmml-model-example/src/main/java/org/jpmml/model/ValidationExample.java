/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.validation.Schema;

import org.dmg.pmml.PMML;

public class ValidationExample extends ConsumptionExample {

	static
	public void main(String... args) throws Exception {
		execute(ValidationExample.class, args);
	}

	@Override
	public Unmarshaller createUnmarshaller() throws JAXBException {
		Unmarshaller unmarshaller = super.createUnmarshaller();

		Schema schema;

		try {
			schema = JAXBUtil.getSchema();
		} catch(Exception e){
			throw new RuntimeException(e);
		}

		unmarshaller.setSchema(schema);
		unmarshaller.setEventHandler(new SimpleValidationEventHandler());

		return unmarshaller;
	}

	@Override
	public void consume(PMML pmml){
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