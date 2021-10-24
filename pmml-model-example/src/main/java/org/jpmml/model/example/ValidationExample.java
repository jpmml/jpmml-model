/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.example;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.validation.Schema;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import org.dmg.pmml.PMML;
import org.jpmml.model.JAXBUtil;

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