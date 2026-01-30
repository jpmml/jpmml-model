/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.UnmarshalException;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XEEAttackTest {

	@Test
	public void unmarshal() throws Exception {

		try(InputStream is = ResourceUtil.getStream(XEEAttackTest.class)){
			JAXBSerializer serializer = new JAXBSerializer();

			Source source = new StreamSource(is);

			UnmarshalException exception = assertThrows(UnmarshalException.class, () -> serializer.unmarshal(source));

			assertInstanceOf(SAXParseException.class, SAXUtil.getCause(exception));
		}
	}
}