/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.Aggregate;
import org.dmg.pmml.BayesInput;
import org.dmg.pmml.BlockIndicator;
import org.dmg.pmml.CategoricalPredictor;
import org.dmg.pmml.ClusteringField;
import org.dmg.pmml.ContinuousNode;
import org.dmg.pmml.DiscreteNode;
import org.dmg.pmml.Discretize;
import org.dmg.pmml.FieldColumnPair;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.FieldRef;
import org.dmg.pmml.FieldValue;
import org.dmg.pmml.FieldValueCount;
import org.dmg.pmml.GeneralRegressionModel;
import org.dmg.pmml.KNNInput;
import org.dmg.pmml.Lag;
import org.dmg.pmml.NormContinuous;
import org.dmg.pmml.NormDiscrete;
import org.dmg.pmml.NumericPredictor;
import org.dmg.pmml.PPCell;
import org.dmg.pmml.ParentValue;
import org.dmg.pmml.Predictor;
import org.dmg.pmml.PredictorTerm;
import org.dmg.pmml.SetPredicate;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.TestDistributions;
import org.dmg.pmml.TextIndex;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.association.Item;

/**
 * <p>
 * A Visitor that determines which fields are referenced during the evaluation of a class model object.
 * </p>
 */
public class FieldReferenceFinder extends AbstractVisitor {

	private Set<FieldName> names = new HashSet<>();


	@Override
	public void applyTo(Visitable visitable){
		this.names.clear();

		super.applyTo(visitable);
	}

	@Override
	public VisitorAction visit(Aggregate aggregate){
		process(aggregate.getField());
		process(aggregate.getGroupField());

		return super.visit(aggregate);
	}

	@Override
	public VisitorAction visit(BayesInput bayesInput){
		process(bayesInput.getFieldName());

		return super.visit(bayesInput);
	}

	@Override
	public VisitorAction visit(BlockIndicator blockIndicator){
		process(blockIndicator.getField());

		return super.visit(blockIndicator);
	}

	@Override
	public VisitorAction visit(CategoricalPredictor categoricalPredictor){
		process(categoricalPredictor.getName());

		return super.visit(categoricalPredictor);
	}

	@Override
	public VisitorAction visit(ClusteringField clusteringField){
		process(clusteringField.getField());

		return super.visit(clusteringField);
	}

	@Override
	public VisitorAction visit(ContinuousNode continuousNode){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(DiscreteNode discreteNode){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(Discretize discretize){
		process(discretize.getField());

		return super.visit(discretize);
	}

	@Override
	public VisitorAction visit(FieldColumnPair fieldColumnPair){
		process(fieldColumnPair.getField());

		return super.visit(fieldColumnPair);
	}

	@Override
	public VisitorAction visit(FieldRef fieldRef){
		process(fieldRef.getField());

		return super.visit(fieldRef);
	}

	@Override
	public VisitorAction visit(FieldValue fieldValue){
		process(fieldValue.getField());

		return super.visit(fieldValue);
	}

	@Override
	public VisitorAction visit(FieldValueCount fieldValueCount){
		process(fieldValueCount.getField());

		return super.visit(fieldValueCount);
	}

	@Override
	public VisitorAction visit(GeneralRegressionModel generalRegressionModel){
		GeneralRegressionModel.ModelType modelType = generalRegressionModel.getModelType();

		switch(modelType){
			case COX_REGRESSION:
				process(generalRegressionModel.getBaselineStrataVariable());
				process(generalRegressionModel.getEndTimeVariable());
				process(generalRegressionModel.getStartTimeVariable());
				process(generalRegressionModel.getStatusVariable());
				process(generalRegressionModel.getSubjectIDVariable());
				// Falls through
			default:
				process(generalRegressionModel.getOffsetVariable());
				process(generalRegressionModel.getTrialsVariable());
				break;
		}

		return super.visit(generalRegressionModel);
	}

	@Override
	public VisitorAction visit(Item item){
		process(item.getField());

		return super.visit(item);
	}

	@Override
	public VisitorAction visit(KNNInput knnInput){
		process(knnInput.getField());

		return super.visit(knnInput);
	}

	@Override
	public VisitorAction visit(Lag lag){
		process(lag.getField());

		return super.visit(lag);
	}

	@Override
	public VisitorAction visit(NormContinuous normContinuous){
		process(normContinuous.getField());

		return super.visit(normContinuous);
	}

	@Override
	public VisitorAction visit(NormDiscrete normDiscrete){
		process(normDiscrete.getField());

		return super.visit(normDiscrete);
	}

	@Override
	public VisitorAction visit(NumericPredictor numericPredictor){
		process(numericPredictor.getName());

		return super.visit(numericPredictor);
	}

	@Override
	public VisitorAction visit(ParentValue parentValue){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(PPCell ppCell){
		process(ppCell.getPredictorName());

		return super.visit(ppCell);
	}

	@Override
	public VisitorAction visit(Predictor predictor){
		process(predictor.getName());

		return super.visit(predictor);
	}

	@Override
	public VisitorAction visit(PredictorTerm predictorTerm){
		process(predictorTerm.getName());

		return super.visit(predictorTerm);
	}

	@Override
	public VisitorAction visit(SetPredicate setPredicate){
		process(setPredicate.getField());

		return super.visit(setPredicate);
	}

	@Override
	public VisitorAction visit(SimplePredicate simplePredicate){
		process(simplePredicate.getField());

		return super.visit(simplePredicate);
	}

	@Override
	public VisitorAction visit(SimpleSetPredicate simpleSetPredicate){
		process(simpleSetPredicate.getField());

		return super.visit(simpleSetPredicate);
	}

	@Override
	public VisitorAction visit(TestDistributions testDistributions){
		process(testDistributions.getField());
		process(testDistributions.getWeightField());

		return super.visit(testDistributions);
	}

	@Override
	public VisitorAction visit(TextIndex textIndex){
		process(textIndex.getTextField());

		return super.visit(textIndex);
	}

	public Set<FieldName> getFieldNames(){
		return this.names;
	}

	private void process(FieldName name){

		if(name == null){
			return;
		}

		this.names.add(name);
	}
}