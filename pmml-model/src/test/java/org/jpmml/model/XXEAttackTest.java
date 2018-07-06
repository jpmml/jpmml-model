/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.UnmarshalException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.PMML;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XXEAttackTest {

	@Test
	public void unmarshal() throws Exception {
		PMML pmml;

		System.setProperty("javax.xml.accessExternalDTD", "file");

		try(InputStream is = ResourceUtil.getStream(XXEAttackTest.class);){
			Source source = new StreamSource(is);

			pmml = JAXBUtil.unmarshalPMML(source);
		} finally {
			System.clearProperty("javax.xml.accessExternalDTD");
		}

		List<?> content = ExtensionUtil.getContent(pmml);

		assertEquals(Arrays.asList("lol"), content);
	}

	@Test
	public void unmarshalSecurely() throws Exception {

		try(InputStream is = ResourceUtil.getStream(XXEAttackTest.class)){
			Source source = SAXUtil.createFilteredSource(is);

			JAXBUtil.unmarshalPMML(source);

			fail();
		} catch(UnmarshalException ue){
			Throwable cause = ue.getCause();

			assertTrue(cause instanceof SAXParseException);
		}
	}
}