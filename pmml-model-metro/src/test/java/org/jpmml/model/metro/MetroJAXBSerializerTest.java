/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Version;
import org.dmg.pmml.adapters.NodeAdapterTest;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.regression.RegressionTable;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.Serializer;
import org.jpmml.model.TextSerializer;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetroJAXBSerializerTest {

	@Test
	public void jaxbClone() throws Exception {
		Serializer serializer = new MetroJAXBSerializer();

		PMML pmml = ResourceUtil.unmarshal(NodeAdapterTest.class);

		checkedClone(serializer, pmml);
	}

	@Test
	public void marshal() throws Exception {
		TextSerializer serializer = new MetroJAXBSerializer();

		PMML pmml = new PMML(Version.PMML_4_4.getVersion(), new Header(), new DataDictionary());

		RegressionModel regressionModel = new RegressionModel()
			.addRegressionTables(new RegressionTable());

		pmml.addModels(regressionModel);

		String string = SerializationUtil.toString(serializer, pmml);

		assertTrue(string.contains("<PMML xmlns=\"http://www.dmg.org/PMML-4_4\""));
		assertTrue(string.contains(" version=\"4.4\">"));
		assertTrue(string.contains("<RegressionModel>"));
		assertTrue(string.contains("</RegressionModel>"));
		assertTrue(string.contains("</PMML>"));
	}

	static
	private <E extends PMMLObject> E checkedClone(Serializer serializer, E object) throws Exception {
		E clonedObject = SerializationUtil.clone(serializer, object);

		assertTrue(ReflectionUtil.equals(object, clonedObject));

		return clonedObject;
	}
}