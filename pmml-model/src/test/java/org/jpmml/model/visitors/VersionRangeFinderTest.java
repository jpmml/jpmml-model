/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;

import org.dmg.pmml.Apply;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.DefineFunction;
import org.dmg.pmml.Header;
import org.dmg.pmml.OpType;
import org.dmg.pmml.Output;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLFunctions;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.ParameterField;
import org.dmg.pmml.Target;
import org.dmg.pmml.TargetValue;
import org.dmg.pmml.Targets;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Version;
import org.dmg.pmml.association.AssociationModel;
import org.dmg.pmml.baseline.BaselineModel;
import org.dmg.pmml.bayesian_network.BayesianNetworkModel;
import org.dmg.pmml.gaussian_process.GaussianProcessModel;
import org.dmg.pmml.general_regression.GeneralRegressionModel;
import org.dmg.pmml.general_regression.PPCell;
import org.dmg.pmml.general_regression.PPMatrix;
import org.dmg.pmml.naive_bayes.NaiveBayesModel;
import org.dmg.pmml.nearest_neighbor.NearestNeighborModel;
import org.dmg.pmml.neural_network.NeuralNetwork;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.rule_set.RuleSetModel;
import org.dmg.pmml.scorecard.Scorecard;
import org.dmg.pmml.sequence.SequenceModel;
import org.dmg.pmml.text.TextModel;
import org.dmg.pmml.time_series.TimeSeriesModel;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionRangeFinderTest {

	@Test
	public void inspectTypeAnnotations(){
		PMML pmml = createPMML();

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);

		pmml.addModels(new AssociationModel(),
			//new ClusteringModel(),
			//new GeneralRegressionModel(),
			//new MiningModel(),
			new NaiveBayesModel(),
			new NeuralNetwork(),
			new RegressionModel(),
			new RuleSetModel(),
			new SequenceModel(),
			//new SupportVectorMachineModel(),
			new TextModel(),
			new TreeModel());

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);

		pmml.addModels(new TimeSeriesModel());

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_4);

		pmml.addModels(new BaselineModel(),
			new Scorecard(),
			new NearestNeighborModel());

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_4);

		pmml.addModels(new BayesianNetworkModel(),
			new GaussianProcessModel());

		assertVersionRange(pmml, Version.PMML_4_3, Version.PMML_4_4);
	}

	@Test
	public void inspectFieldAnnotations(){
		PMML pmml = createPMML();

		AssociationModel model = new AssociationModel();

		pmml.addModels(model);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);

		Output output = new Output();

		model.setOutput(output);

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_4);

		model.setScorable(Boolean.FALSE);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_4);

		model.setScorable(null);

		assertVersionRange(pmml, Version.PMML_4_0, Version.PMML_4_4);

		OutputField outputField = new OutputField()
			.setRuleFeature(OutputField.RuleFeature.AFFINITY);

		output.addOutputFields(outputField);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_2);

		outputField.setDataType(DataType.DOUBLE);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_4);

		model.setOutput(null);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);
	}

	@Test
	public void inspectValueAnnotations(){
		PMML pmml = createPMML();

		Target target = new Target()
			.setTargetField("y")
			.addTargetValues(createTargetValue("no event"), createTargetValue("event"));

		Targets targets = new Targets()
			.addTargets(target);

		GeneralRegressionModel model = new GeneralRegressionModel()
			.setTargets(targets);

		pmml.addModels(model);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_3_0);

		PPMatrix ppMatrix = new PPMatrix()
			.addPPCells(new PPCell(), new PPCell());

		model.setPPMatrix(ppMatrix);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);

		target.setTargetField(null);

		assertVersionRange(pmml, Version.PMML_4_3, Version.PMML_4_4);
	}

	@Test
	public void inspectFunctions(){
		PMML pmml = createPMML();

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);

		Apply apply = new Apply()
			.setFunction(PMMLFunctions.LOWERCASE);

		DefineFunction defineFunction = new DefineFunction("convert_case", OpType.CATEGORICAL, DataType.STRING, null, apply)
			.addParameterFields(new ParameterField("string"));

		TransformationDictionary transformationDictionary = new TransformationDictionary()
			.addDefineFunctions(defineFunction);

		pmml.setTransformationDictionary(transformationDictionary);

		assertVersionRange(pmml, Version.PMML_4_1, Version.PMML_4_4);

		apply.setFunction(PMMLFunctions.UPPERCASE);

		assertVersionRange(pmml, Version.PMML_3_0, Version.PMML_4_4);
	}

	static
	private PMML createPMML(){
		Header header = new Header()
			.setCopyright("ACME Corporation");

		DataField dataField = new DataField("x", OpType.CATEGORICAL, DataType.DOUBLE);

		DataDictionary dataDictionary = new DataDictionary()
			.addDataFields(dataField);

		PMML pmml = new PMML(Version.PMML_4_4.getVersion(), header, dataDictionary);

		return pmml;
	}

	static
	private TargetValue createTargetValue(String value){
		TargetValue targetValue = new TargetValue()
			.setValue(value);

		return targetValue;
	}

	static
	private void assertVersionRange(PMMLObject object, Version minimum, Version maximum){
		VersionRangeFinder inspector = new VersionRangeFinder();
		inspector.applyTo(object);

		assertEquals(Arrays.asList(minimum, maximum), Arrays.asList(inspector.getMinimum(), inspector.getMaximum()));
	}
}
