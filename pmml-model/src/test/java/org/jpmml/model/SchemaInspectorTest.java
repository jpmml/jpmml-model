/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.*;

import org.jpmml.schema.*;

import org.dmg.pmml.*;

import org.junit.*;

import static org.junit.Assert.*;

public class SchemaInspectorTest {

	@Test
	public void inspectTypeAnnotations(){
		PMML pmml = new PMML(new Header(), new DataDictionary(), "4.2");

		List<Model> models = pmml.getModels();

		assertEquals(0, models.size());

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_2);

		pmml.withModels(new AssociationModel(),
			new ClusteringModel(),
			new GeneralRegressionModel(),
			new MiningModel(),
			new NaiveBayesModel(),
			new NeuralNetwork(),
			new RegressionModel(),
			new RuleSetModel(),
			new SequenceModel(),
			new SupportVectorMachineModel(),
			new TextModel(),
			new TreeModel());

		assertEquals(12, models.size());

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_2);

		pmml.withModels(new TimeSeriesModel());

		assertEquals(13, models.size());

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_2);

		pmml.withModels(new BaselineModel(),
			new Scorecard(),
			new NearestNeighborModel());

		assertEquals(16, models.size());

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_2);
	}

	@Test
	public void inspectFieldAnnotations(){
		PMML pmml = new PMML(new Header(), new DataDictionary(), "4.2");

		AssociationModel model = new AssociationModel();

		pmml.withModels(model);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_2);

		Output output = new Output();
		model.withOutput(output);

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_2);

		model.setScorable(Boolean.FALSE);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_2);

		model.setScorable(null);

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_2);

		OutputField outputField = new OutputField()
			.withRuleFeature(RuleFeatureType.AFFINITY);

		output.withOutputFields(outputField);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_2);

		model.setOutput(null);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_2);
	}

	static
	private void assertVersionRange(PMMLObject object, Version minimum, Version maximum){
		SchemaInspector inspector = new SchemaInspector();

		object.accept(inspector);

		assertEquals(minimum, inspector.getMinimum());
		assertEquals(maximum, inspector.getMaximum());
	}
}