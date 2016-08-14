/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.MiningModel;
import org.dmg.pmml.Model;
import org.dmg.pmml.Scorecard;
import org.dmg.pmml.SequenceModel;
import org.dmg.pmml.SupportVectorMachineModel;
import org.dmg.pmml.TextModel;
import org.dmg.pmml.TimeSeriesModel;
import org.dmg.pmml.TreeModel;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.association.AssociationModel;
import org.dmg.pmml.baseline.BaselineModel;
import org.dmg.pmml.bayesian_network.BayesianNetworkModel;
import org.dmg.pmml.clustering.ClusteringModel;
import org.dmg.pmml.gaussian_process.GaussianProcessModel;
import org.dmg.pmml.general_regression.GeneralRegressionModel;
import org.dmg.pmml.naive_bayes.NaiveBayesModel;
import org.dmg.pmml.nearest_neighbor.NearestNeighborModel;
import org.dmg.pmml.neural_network.NeuralNetwork;
import org.dmg.pmml.regression.RegressionModel;
import org.dmg.pmml.rule_set.RuleSetModel;

abstract
public class AbstractModelVisitor extends AbstractVisitor {

	abstract
	public VisitorAction visit(Model model);

	@Override
	public VisitorAction visit(AssociationModel associationModel){
		return visit((Model)associationModel);
	}

	@Override
	public VisitorAction visit(BaselineModel baselineModel){
		return visit((Model)baselineModel);
	}

	@Override
	public VisitorAction visit(BayesianNetworkModel bayesianNetworkModel){
		return visit((Model)bayesianNetworkModel);
	}

	@Override
	public VisitorAction visit(ClusteringModel clusteringModel){
		return visit((Model)clusteringModel);
	}

	@Override
	public VisitorAction visit(GaussianProcessModel gaussianProcessModel){
		return visit((Model)gaussianProcessModel);
	}

	@Override
	public VisitorAction visit(GeneralRegressionModel generalRegressionModel){
		return visit((Model)generalRegressionModel);
	}

	@Override
	public VisitorAction visit(MiningModel miningModel){
		return visit((Model)miningModel);
	}

	@Override
	public VisitorAction visit(NaiveBayesModel naiveBayesModel){
		return visit((Model)naiveBayesModel);
	}

	@Override
	public VisitorAction visit(NearestNeighborModel nearestNeighborModel){
		return visit((Model)nearestNeighborModel);
	}

	@Override
	public VisitorAction visit(NeuralNetwork neuralNetwork){
		return visit((Model)neuralNetwork);
	}

	@Override
	public VisitorAction visit(RegressionModel regressionModel){
		return visit((Model)regressionModel);
	}

	@Override
	public VisitorAction visit(RuleSetModel ruleSetModel){
		return visit((Model)ruleSetModel);
	}

	@Override
	public VisitorAction visit(Scorecard scorecard){
		return visit((Model)scorecard);
	}

	@Override
	public VisitorAction visit(SequenceModel sequenceModel){
		return visit((Model)sequenceModel);
	}

	@Override
	public VisitorAction visit(SupportVectorMachineModel supportVectorMachineModel){
		return visit((Model)supportVectorMachineModel);
	}

	@Override
	public VisitorAction visit(TextModel textModel){
		return visit((Model)textModel);
	}

	@Override
	public VisitorAction visit(TimeSeriesModel timeSeriesModel){
		return visit((Model)timeSeriesModel);
	}

	@Override
	public VisitorAction visit(TreeModel treeModel){
		return visit((Model)treeModel);
	}
}