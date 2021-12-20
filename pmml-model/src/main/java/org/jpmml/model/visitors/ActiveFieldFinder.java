/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.Aggregate;
import org.dmg.pmml.BlockIndicator;
import org.dmg.pmml.Discretize;
import org.dmg.pmml.FieldColumnPair;
import org.dmg.pmml.FieldRef;
import org.dmg.pmml.Lag;
import org.dmg.pmml.Model;
import org.dmg.pmml.NormContinuous;
import org.dmg.pmml.NormDiscrete;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.TextIndex;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.association.Item;
import org.dmg.pmml.baseline.FieldValue;
import org.dmg.pmml.baseline.FieldValueCount;
import org.dmg.pmml.baseline.TestDistributions;
import org.dmg.pmml.bayesian_network.ContinuousNode;
import org.dmg.pmml.bayesian_network.DiscreteNode;
import org.dmg.pmml.bayesian_network.ParentValue;
import org.dmg.pmml.clustering.ClusteringField;
import org.dmg.pmml.general_regression.GeneralRegressionModel;
import org.dmg.pmml.general_regression.PPCell;
import org.dmg.pmml.general_regression.Predictor;
import org.dmg.pmml.mining.VariableWeight;
import org.dmg.pmml.naive_bayes.BayesInput;
import org.dmg.pmml.nearest_neighbor.KNNInput;
import org.dmg.pmml.regression.CategoricalPredictor;
import org.dmg.pmml.regression.NumericPredictor;
import org.dmg.pmml.sequence.SetPredicate;

/**
 * <p>
 * A Visitor that determines which fields are referenced during the evaluation of a class model object.
 * </p>
 *
 * @see HasActiveFields
 */
public class ActiveFieldFinder extends AbstractVisitor implements Resettable {

	private Set<String> names = null;


	@Override
	public void reset(){

		if(this.names != null){

			if(this.names.size() == 1){
				this.names = null;

				return;
			}

			this.names.clear();
		}
	}

	@Override
	public VisitorAction visit(Aggregate aggregate){
		process(aggregate.getField());
		process(aggregate.getGroupField());

		return super.visit(aggregate);
	}

	@Override
	public VisitorAction visit(BayesInput bayesInput){
		process(bayesInput.getField());

		return super.visit(bayesInput);
	}

	@Override
	public VisitorAction visit(BlockIndicator blockIndicator){
		process(blockIndicator.getField());

		return super.visit(blockIndicator);
	}

	@Override
	public VisitorAction visit(CategoricalPredictor categoricalPredictor){
		process(categoricalPredictor.getField());

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
		GeneralRegressionModel.ModelType modelType = generalRegressionModel.requireModelType();

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
	public VisitorAction visit(Model model){

		if(model instanceof HasActiveFields){
			HasActiveFields hasActiveFields = (HasActiveFields)model;

			Set<String> names = hasActiveFields.getActiveFields();
			for(String name : names){
				process(name);
			}
		}

		return super.visit(model);
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
		process(numericPredictor.getField());

		return super.visit(numericPredictor);
	}

	@Override
	public VisitorAction visit(ParentValue parentValue){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(PPCell ppCell){
		process(ppCell.getField());

		return super.visit(ppCell);
	}

	@Override
	public VisitorAction visit(Predictor predictor){
		process(predictor.getField());

		return super.visit(predictor);
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

	@Override
	public VisitorAction visit(VariableWeight variableWeight){
		process(variableWeight.getField());

		return super.visit(variableWeight);
	}

	public Set<String> getFieldNames(){

		if(this.names == null){
			return Collections.emptySet();
		}

		return Collections.unmodifiableSet(this.names);
	}

	private void process(String name){

		if(name == null){
			return;
		} // End if

		if(this.names != null){

			if(this.names.size() == 1){

				if(this.names.contains(name)){
					return;
				}

				this.names = new HashSet<>(this.names);
			}

			this.names.add(name);
		} else

		{
			this.names = Collections.singleton(name);
		}
	}

	static
	public Set<String> getFieldNames(PMMLObject... objects){
		return getFieldNames(new ActiveFieldFinder(), objects);
	}

	static
	public Set<String> getFieldNames(ActiveFieldFinder activeFieldFinder, PMMLObject... objects){

		for(PMMLObject object : objects){
			activeFieldFinder.applyTo(object);
		}

		return activeFieldFinder.getFieldNames();
	}
}