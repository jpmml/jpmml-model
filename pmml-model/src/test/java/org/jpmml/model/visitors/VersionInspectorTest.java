/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;

import org.dmg.pmml.Apply;
import org.dmg.pmml.AssociationModel;
import org.dmg.pmml.BaselineModel;
import org.dmg.pmml.BayesianNetworkModel;
import org.dmg.pmml.ClusteringModel;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DefineFunction;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.GaussianProcessModel;
import org.dmg.pmml.GeneralRegressionModel;
import org.dmg.pmml.Header;
import org.dmg.pmml.MiningModel;
import org.dmg.pmml.Model;
import org.dmg.pmml.NaiveBayesModel;
import org.dmg.pmml.NearestNeighborModel;
import org.dmg.pmml.NeuralNetwork;
import org.dmg.pmml.OpType;
import org.dmg.pmml.Output;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.ParameterField;
import org.dmg.pmml.RegressionModel;
import org.dmg.pmml.RuleFeatureType;
import org.dmg.pmml.RuleSetModel;
import org.dmg.pmml.Scorecard;
import org.dmg.pmml.SequenceModel;
import org.dmg.pmml.SupportVectorMachineModel;
import org.dmg.pmml.TextModel;
import org.dmg.pmml.TimeSeriesModel;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.TreeModel;
import org.jpmml.schema.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionInspectorTest {

	@Test
	public void inspectTypeAnnotations(){
		PMML pmml = new PMML("4.3", new Header(), new DataDictionary());

		List<Model> models = pmml.getModels();

		assertEquals(0, models.size());

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_3);

		pmml.addModels(new AssociationModel(),
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

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_3);

		pmml.addModels(new TimeSeriesModel());

		assertEquals(13, models.size());

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_3);

		pmml.addModels(new BaselineModel(),
			new Scorecard(),
			new NearestNeighborModel());

		assertEquals(16, models.size());

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_3);

		pmml.addModels(new BayesianNetworkModel(),
			new GaussianProcessModel());

		assertVersionRange(pmml, Version.PMML_4_3, Version.PMML_4_3);
	}

	@Test
	public void inspectFieldAnnotations(){
		PMML pmml = new PMML("4.3", new Header(), new DataDictionary());

		AssociationModel model = new AssociationModel();

		pmml.addModels(model);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_3);

		Output output = new Output();

		model.setOutput(output);

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_3);

		model.setScorable(Boolean.FALSE);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_3);

		model.setScorable(null);

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_3);

		OutputField outputField = new OutputField()
			.setRuleFeature(RuleFeatureType.AFFINITY);

		output.addOutputFields(outputField);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_3);

		model.setOutput(null);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_3);
	}

	@Test
	public void inspectFunctions(){
		PMML pmml = new PMML("4.3", new Header(), new DataDictionary());

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_3);

		Apply apply = new Apply()
			.setFunction("lowercase");

		DefineFunction defineFunction = new DefineFunction("convert_case", OpType.CATEGORICAL, null)
			.addParameterFields(new ParameterField(FieldName.create("string")))
			.setExpression(apply);

		TransformationDictionary transformationDictionary = new TransformationDictionary()
			.addDefineFunctions(defineFunction);

		pmml.setTransformationDictionary(transformationDictionary);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_3);

		apply.setFunction("uppercase");

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_3);
	}

	static
	private void assertVersionRange(PMMLObject object, Version minimum, Version maximum){
		VersionInspector inspector = new VersionInspector();
		inspector.applyTo(object);

		assertEquals(minimum, inspector.getMinimum());
		assertEquals(maximum, inspector.getMaximum());
	}
}
