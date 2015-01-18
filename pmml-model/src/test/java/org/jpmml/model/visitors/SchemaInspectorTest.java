/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;

import org.dmg.pmml.Apply;
import org.dmg.pmml.AssociationModel;
import org.dmg.pmml.BaselineModel;
import org.dmg.pmml.ClusteringModel;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DefineFunction;
import org.dmg.pmml.FieldName;
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

	@Test
	public void inspectFunctions(){
		PMML pmml = new PMML(new Header(), new DataDictionary(), "4.2");

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_2);

		Apply apply = new Apply();
		apply.setFunction("lowercase");

		DefineFunction defineFunction = new DefineFunction("convert_case", OpType.CATEGORICAL)
			.withParameterFields(new ParameterField(new FieldName("string")))
			.withExpression(apply);

		TransformationDictionary transformationDictionary = new TransformationDictionary()
			.withDefineFunctions(defineFunction);

		pmml.withTransformationDictionary(transformationDictionary);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_2);

		apply.setFunction("uppercase");

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