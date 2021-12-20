/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.association.AssociationModel;
import org.dmg.pmml.association.Itemset;
import org.dmg.pmml.clustering.ClusteringModel;
import org.dmg.pmml.neural_network.NeuralInputs;
import org.dmg.pmml.neural_network.NeuralLayer;
import org.dmg.pmml.neural_network.NeuralNetwork;
import org.dmg.pmml.neural_network.NeuralOutputs;
import org.dmg.pmml.support_vector_machine.Coefficients;
import org.dmg.pmml.support_vector_machine.SupportVectors;
import org.dmg.pmml.support_vector_machine.VectorDictionary;
import org.dmg.pmml.support_vector_machine.VectorFields;
import org.jpmml.model.InvalidElementException;
import org.jpmml.model.InvalidMarkupException;

/**
 * <p>
 * A Visitor that inspects a class model object for invalid features.
 * </p>
 *
 * @see MarkupInspector#applyTo(Visitable)
 * @see InvalidMarkupException
 */
public class InvalidMarkupInspector extends MarkupInspector<InvalidMarkupException> {

	@Override
	public VisitorAction visit(AssociationModel associationModel){
		check(new CollectionSize(associationModel){

			@Override
			public Integer getSize(){
				return associationModel.getNumberOfItems();
			}

			@Override
			public Collection<?> getCollection(){
				return associationModel.getItems();
			}

			@Override
			public boolean evaluate(int left, int right){
				// "The numberOfItems attribute may be greater than or equal to the number of items contained in the model"
				return (left >= right);
			}
		});

		check(new CollectionSize(associationModel){

			@Override
			public Integer getSize(){
				return associationModel.getNumberOfItemsets();
			}

			@Override
			public Collection<?> getCollection(){
				return associationModel.getItemsets();
			}
		});

		check(new CollectionSize(associationModel){

			@Override
			public Integer getSize(){
				return associationModel.getNumberOfRules();
			}

			@Override
			public Collection<?> getCollection(){
				return associationModel.getAssociationRules();
			}
		});

		return super.visit(associationModel);
	}

	@Override
	public VisitorAction visit(ClusteringModel clusteringModel){
		check(new CollectionSize(clusteringModel){

			@Override
			public Integer getSize(){
				return clusteringModel.getNumberOfClusters();
			}

			@Override
			public Collection<?> getCollection(){
				return clusteringModel.getClusters();
			}
		});

		return super.visit(clusteringModel);
	}

	@Override
	public VisitorAction visit(Coefficients coefficients){
		check(new CollectionSize(coefficients){

			@Override
			public Integer getSize(){
				return coefficients.getNumberOfCoefficients();
			}

			@Override
			public Collection<?> getCollection(){
				return coefficients.getCoefficients();
			}
		});

		return super.visit(coefficients);
	}

	@Override
	public VisitorAction visit(DataDictionary dataDictionary){
		check(new CollectionSize(dataDictionary){

			@Override
			public Integer getSize(){
				return dataDictionary.getNumberOfFields();
			}

			@Override
			public Collection<?> getCollection(){
				return dataDictionary.getDataFields();
			}
		});

		return super.visit(dataDictionary);
	}

	@Override
	public VisitorAction visit(Itemset itemset){
		check(new CollectionSize(itemset){

			@Override
			public Integer getSize(){
				return itemset.getNumberOfItems();
			}

			@Override
			public Collection<?> getCollection(){
				return itemset.getItemRefs();
			}
		});

		return super.visit(itemset);
	}

	@Override
	public VisitorAction visit(NeuralInputs neuralInputs){
		check(new CollectionSize(neuralInputs){

			@Override
			public Integer getSize(){
				return neuralInputs.getNumberOfInputs();
			}

			@Override
			public Collection<?> getCollection(){
				return neuralInputs.getNeuralInputs();
			}
		});

		return super.visit(neuralInputs);
	}

	@Override
	public VisitorAction visit(NeuralLayer neuralLayer){
		check(new CollectionSize(neuralLayer){

			@Override
			public Integer getSize(){
				return neuralLayer.getNumberOfNeurons();
			}

			@Override
			public Collection<?> getCollection(){
				return neuralLayer.getNeurons();
			}
		});

		return super.visit(neuralLayer);
	}

	@Override
	public VisitorAction visit(NeuralNetwork neuralNetwork){
		check(new CollectionSize(neuralNetwork){

			@Override
			public Integer getSize(){
				return neuralNetwork.getNumberOfLayers();
			}

			@Override
			public Collection<?> getCollection(){
				return neuralNetwork.getNeuralLayers();
			}
		});

		return super.visit(neuralNetwork);
	}

	@Override
	public VisitorAction visit(NeuralOutputs neuralOutputs){
		check(new CollectionSize(neuralOutputs){

			@Override
			public Integer getSize(){
				return neuralOutputs.getNumberOfOutputs();
			}

			@Override
			public Collection<?> getCollection(){
				return neuralOutputs.getNeuralOutputs();
			}
		});

		return super.visit(neuralOutputs);
	}

	@Override
	public VisitorAction visit(SupportVectors supportVectors){
		check(new CollectionSize(supportVectors){

			@Override
			public Integer getSize(){
				return supportVectors.getNumberOfSupportVectors();
			}

			@Override
			public Collection<?> getCollection(){
				return supportVectors.getSupportVectors();
			}
		});

		return super.visit(supportVectors);
	}

	@Override
	public VisitorAction visit(VectorDictionary vectorDictionary){
		check(new CollectionSize(vectorDictionary){

			@Override
			public Integer getSize(){
				return vectorDictionary.getNumberOfVectors();
			}

			@Override
			public Collection<?> getCollection(){
				return vectorDictionary.getVectorInstances();
			}
		});

		return super.visit(vectorDictionary);
	}

	@Override
	public VisitorAction visit(VectorFields vectorFields){
		check(new CollectionSize(vectorFields){

			@Override
			public Integer getSize(){
				return vectorFields.getNumberOfFields();
			}

			@Override
			public Collection<?> getCollection(){
				return vectorFields.getContent();
			}
		});

		return super.visit(vectorFields);
	}

	private void check(Condition condition){
		boolean result = condition.evaluate();

		if(!result){
			PMMLObject object = condition.getObject();

			report(new InvalidElementException(object));
		}
	}

	abstract
	private class Condition {

		private PMMLObject object = null;


		public Condition(PMMLObject object){
			setObject(object);
		}

		abstract
		public boolean evaluate();

		public PMMLObject getObject(){
			return this.object;
		}

		private void setObject(PMMLObject object){

			if(object == null){
				throw new IllegalArgumentException();
			}

			this.object = object;
		}
	}

	abstract
	private class CollectionSize extends Condition {

		public CollectionSize(PMMLObject object){
			super(object);
		}

		abstract
		public Integer getSize();

		abstract
		public Collection<?> getCollection();

		@Override
		public boolean evaluate(){
			Integer size = getSize();

			if(size != null){
				Collection<?> collection = getCollection();

				return evaluate(size, collection.size());
			}

			return true;
		}

		public boolean evaluate(int left, int right){
			return (left == right);
		}
	}
}
