/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.*;

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class SchemaInspectorTest {

	@Test
	public void inspect(){
		PMML pmml = new PMML(new Header(), new DataDictionary(), "4.2");

		List<Model> models = pmml.getModels();

		SchemaInspector inspector = new SchemaInspector();

		pmml.accept(inspector);

		assertEquals(Version.PMML_3_0, inspector.getMinimum());
		assertEquals(Version.PMML_4_2, inspector.getMaximum());

		models.add(new AssociationModel());
		models.add(new ClusteringModel());
		models.add(new GeneralRegressionModel());
		models.add(new MiningModel());
		models.add(new NaiveBayesModel());
		models.add(new NeuralNetwork());
		models.add(new RegressionModel());
		models.add(new RuleSetModel());
		models.add(new SequenceModel());
		models.add(new SupportVectorMachineModel());
		models.add(new TextModel());
		models.add(new TreeModel());

		pmml.accept(inspector);

		assertEquals(Version.PMML_3_0, inspector.getMinimum());
		assertEquals(Version.PMML_4_2, inspector.getMaximum());

		models.add(new TimeSeriesModel());

		pmml.accept(inspector);

		assertEquals(Version.PMML_4_0, inspector.getMinimum());
		assertEquals(Version.PMML_4_2, inspector.getMaximum());

		models.add(new BaselineModel());
		models.add(new Scorecard());
		models.add(new NearestNeighborModel());

		pmml.accept(inspector);

		assertEquals(Version.PMML_4_1, inspector.getMinimum());
		assertEquals(Version.PMML_4_2, inspector.getMaximum());
	}
}