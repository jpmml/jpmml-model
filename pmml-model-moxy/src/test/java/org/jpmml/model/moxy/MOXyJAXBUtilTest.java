/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.moxy;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.Source;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.dmg.pmml.adapters.NodeAdapterTest;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.regression.RegressionTable;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.SAXUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MOXyJAXBUtilTest {

	@Test
	public void jaxbClone() throws Exception {
		PMML pmml;

		JAXBContext context = JAXBContextFactory.createContext(JAXBUtil.getObjectFactoryClasses(), null);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		try(InputStream is = ResourceUtil.getStream(NodeAdapterTest.class)){
			Source source = SAXUtil.createFilteredSource(is);

			pmml = (PMML)unmarshaller.unmarshal(source);
		}

		PMML clonedPmml = JAXBUtil.clone(context, pmml);

		assertTrue(ReflectionUtil.equals(pmml, clonedPmml));
	}

	@Test
	public void marshal() throws Exception {
		PMML pmml = new PMML(Version.PMML_4_4.getVersion(), new Header(), new DataDictionary());

		RegressionModel regressionModel = new RegressionModel()
			.addRegressionTables(new RegressionTable());

		pmml.addModels(regressionModel);

		JAXBContext context = JAXBContextFactory.createContext(JAXBUtil.getObjectFactoryClasses(), null);

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