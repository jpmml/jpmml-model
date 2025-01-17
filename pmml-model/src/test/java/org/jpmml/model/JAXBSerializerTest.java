/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import jakarta.xml.bind.JAXBContext;
import org.dmg.pmml.CustomObjectFactory;
import org.dmg.pmml.CustomPMML;
import org.dmg.pmml.ObjectFactory;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JAXBSerializerTest extends SerializerTest {

	@Test
	public void defaultContextClone() throws Exception {
		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);

		Serializer serializer = new JAXBSerializer(context);

		PMML pmml;

		try(InputStream is = ResourceUtil.getStream(Version.PMML_4_4)){
			pmml = (PMML)serializer.deserialize(is);
		}

		PMML clonedPmml = clone(serializer, pmml);

		assertTrue(ReflectionUtil.equals(pmml, clonedPmml));
	}

	@Test
	public void customContextClone() throws Exception {
		JAXBContext context = JAXBContext.newInstance(CustomObjectFactory.class);

		Serializer serializer = new JAXBSerializer(context);

		CustomPMML pmml;

		try(InputStream is = ResourceUtil.getStream(Version.PMML_4_4)){
			pmml = (CustomPMML)serializer.deserialize(is);
		}

		CustomPMML clonedPmml = clone(serializer, pmml);

		assertTrue(ReflectionUtil.equals(pmml, clonedPmml));
	}
}