/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.UnmarshalException;
import org.dmg.pmml.PMML;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class XXEAttackTest {

	@Test
	public void unmarshal() throws Exception {
		PMML pmml;

		System.setProperty("javax.xml.accessExternalDTD", "file");

		try(InputStream is = ResourceUtil.getStream(XXEAttackTest.class)){
			JAXBSerializer serializer = new JAXBSerializer();

			Source source = new StreamSource(is);

			pmml = (PMML)serializer.unmarshal(source);
		} finally {
			System.clearProperty("javax.xml.accessExternalDTD");
		}

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals(Arrays.asList("lol"), content);
	}

	@Test
	public void unmarshalSecurely() throws Exception {

		try(InputStream is = ResourceUtil.getStream(XXEAttackTest.class)){
			JAXBSerializer serializer = new JAXBSerializer();

			Source source = SAXUtil.createFilteredSource(is);

			serializer.unmarshal(source);

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = SAXUtil.getCause(ue);

			assertTrue(cause instanceof SAXParseException);
		}
	}
}