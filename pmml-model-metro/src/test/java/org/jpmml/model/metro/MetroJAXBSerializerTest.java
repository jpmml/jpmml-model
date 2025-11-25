/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.NamespaceURIs;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.SimplifyingScoreDistributionTransformer;
import org.dmg.pmml.Version;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.regression.RegressionTable;
import org.dmg.pmml.tree.SimplifyingNodeTransformer;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.Serializer;
import org.jpmml.model.TextSerializer;
import org.jpmml.model.resources.NodePolymorphismTest;
import org.jpmml.model.resources.ScoreDistributionPolymorphismTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetroJAXBSerializerTest {

	@Test
	public void nodePolymorphism() throws Exception {
		JAXBSerializer serializer = new MetroJAXBSerializer();

		PMML pmml = NodePolymorphismTest.load(serializer, SimplifyingNodeTransformer.INSTANCE);

		NodePolymorphismTest.checkSimplified(pmml);

		checkedClone(serializer, pmml);
	}

	@Test
	public void scoreDistributionPolymorphism() throws Exception {
		JAXBSerializer serializer = new MetroJAXBSerializer();

		PMML pmml = ScoreDistributionPolymorphismTest.load(serializer, SimplifyingScoreDistributionTransformer.INSTANCE);

		ScoreDistributionPolymorphismTest.checkSimplified(pmml);

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

		assertTrue(string.contains("<PMML xmlns=\"" + NamespaceURIs.PMML_LATEST + "\""));
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