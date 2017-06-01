/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.regression.RegressionTable;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.jpmml.schema.Version;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MarshallerTest {

	@Test
	public void marshal() throws Exception {
		RegressionModel regressionModel = new RegressionModel()
			.addRegressionTables(new RegressionTable());

		PMML pmml = new PMML(Version.PMML_4_3.getVersion(), new Header(), new DataDictionary())
			.addModels(regressionModel);

		JAXBContext context = JAXBContextFactory.createContext(new Class[]{org.dmg.pmml.ObjectFactory.class, org.dmg.pmml.regression.ObjectFactory.class}, null);

		Marshaller marshaller = context.createMarshaller();

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		marshaller.marshal(pmml, os);

		String string = os.toString("UTF-8");

		assertTrue(string.contains("<PMML xmlns=\"http://www.dmg.org/PMML-4_3\" version=\"4.3\">"));
		assertTrue(string.contains("<RegressionModel>"));
		assertTrue(string.contains("</RegressionModel>"));
		assertTrue(string.contains("</PMML>"));
	}
}