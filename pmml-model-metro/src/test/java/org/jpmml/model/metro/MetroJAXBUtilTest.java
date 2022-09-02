/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import java.io.ByteArrayOutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.dmg.pmml.adapters.NodeAdapterTest;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.regression.RegressionTable;
import org.glassfish.jaxb.runtime.v2.ContextFactory;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MetroJAXBUtilTest {

	@Test
	public void jaxbClone() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(NodeAdapterTest.class);

		JAXBContext context = ContextFactory.createContext(JAXBUtil.getObjectFactoryClasses(), null);

		PMML clonedPmml = JAXBUtil.clone(context, pmml);

		assertTrue(ReflectionUtil.equals(pmml, clonedPmml));
	}

	@Test
	public void marshal() throws Exception {
		PMML pmml = new PMML(Version.PMML_4_4.getVersion(), new Header(), new DataDictionary());

		RegressionModel regressionModel = new RegressionModel()
			.addRegressionTables(new RegressionTable());

		pmml.addModels(regressionModel);

		JAXBContext context = ContextFactory.createContext(JAXBUtil.getObjectFactoryClasses(), null);

		Marshaller marshaller = context.createMarshaller();

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			marshaller.marshal(pmml, os);

			string = os.toString("UTF-8");
		}

		assertTrue(string.contains("<PMML xmlns=\"https://www.dmg.org/PMML-4_4\""));
		assertTrue(string.contains(" version=\"4.4\">"));
		assertTrue(string.contains("<RegressionModel>"));
		assertTrue(string.contains("</RegressionModel>"));
		assertTrue(string.contains("</PMML>"));
	}
}