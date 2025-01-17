/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import java.io.ByteArrayOutputStream;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.dmg.pmml.adapters.NodeAdapterTest;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.regression.RegressionTable;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.Serializer;
import org.jpmml.model.SerializerTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MetroJAXBSerializerTest extends SerializerTest {

	@Test
	public void jaxbClone() throws Exception {
		Serializer serializer = new MetroJAXBSerializer();

		PMML pmml = ResourceUtil.unmarshal(NodeAdapterTest.class);

		PMML clonedPmml = clone(serializer, pmml);

		assertTrue(ReflectionUtil.equals(pmml, clonedPmml));
	}

	@Test
	public void marshal() throws Exception {
		Serializer serializer = new MetroJAXBSerializer();

		PMML pmml = new PMML(Version.PMML_4_4.getVersion(), new Header(), new DataDictionary());

		RegressionModel regressionModel = new RegressionModel()
			.addRegressionTables(new RegressionTable());

		pmml.addModels(regressionModel);

		String string;

		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			serializer.serialize(pmml, os);

			string = os.toString("UTF-8");
		}

		assertTrue(string.contains("<PMML xmlns=\"http://www.dmg.org/PMML-4_4\""));
		assertTrue(string.contains(" version=\"4.4\">"));
		assertTrue(string.contains("<RegressionModel>"));
		assertTrue(string.contains("</RegressionModel>"));
		assertTrue(string.contains("</PMML>"));
	}
}