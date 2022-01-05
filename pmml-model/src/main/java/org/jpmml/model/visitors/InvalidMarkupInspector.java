/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;

import org.dmg.pmml.Apply;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Expression;
import org.dmg.pmml.Interval;
import org.dmg.pmml.InvalidValueTreatmentMethod;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MissingValueTreatmentMethod;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.ResultFeature;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.TargetValue;
import org.dmg.pmml.TextIndexNormalization;
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
import org.jpmml.model.InvalidAttributeException;
import org.jpmml.model.InvalidElementException;
import org.jpmml.model.InvalidMarkupException;
import org.jpmml.model.MisplacedAttributeException;
import org.jpmml.model.MisplacedElementException;

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
	public VisitorAction visit(Apply apply){
		InvalidValueTreatmentMethod invalidValueTreatmentMethod = apply.getInvalidValueTreatment();

		switch(invalidValueTreatmentMethod){
			case AS_VALUE:
				report(new InvalidAttributeException(apply, invalidValueTreatmentMethod));
				break;
			default:
				break;
		}

		return super.visit(apply);
	}

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
	public VisitorAction visit(Interval interval){
		Number leftMargin = interval.getLeftMargin();
		Number rightMargin = interval.getRightMargin();

		if((leftMargin != null && rightMargin != null) && Double.compare(leftMargin.doubleValue(), rightMargin.doubleValue()) > 0){
			report(new InvalidElementException(interval));
		}

		return super.visit(interval);
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
	public VisitorAction visit(MiningField miningField){
		InvalidValueTreatmentMethod invalidValueTreatmentMethod = miningField.getInvalidValueTreatment();
		Object invalidValueReplacement = miningField.getInvalidValueReplacement();

		switch(invalidValueTreatmentMethod){
			case AS_IS:
				// XXX: Non-standard behaviour
				if(invalidValueReplacement != null){
					// The JPMML family of libraries introduced invalid value replacement as a vendor extension in PMML schema version 4.3;
					// the implementation was based on the MiningField@(x-)invalidValueReplacement attribute alone:
					// <MiningField name="x" invalidValueTreatment="asIs" x-invalidValueReplacement="0"/>
					// DMG.org introduced invalid value replacement in PMML schema version 4.4;
					// the implementation uses an invalid value treatment method enum constant "asValue" to signal the availability of the MiningField@invalidValueReplacement attribute:
					// <MiningField name="x" invalidValueTreatment="asValue" invalidValueReplacement="0"/>
					// According to the PMML specification, the MiningField@invalidValueReplacement attribute should not be used with the IVTM enum constant "asIs".
					break;
				}
				break;
			case AS_MISSING:
			case RETURN_INVALID:
				if(invalidValueReplacement != null){
					throw new MisplacedAttributeException(miningField, PMMLAttributes.MININGFIELD_INVALIDVALUEREPLACEMENT, invalidValueReplacement);
				}
				break;
			default:
				break;
		}

		MissingValueTreatmentMethod missingValueTreatmentMethod = miningField.getMissingValueTreatment();
		Object missingValueReplacement = miningField.getMissingValueReplacement();

		if(missingValueTreatmentMethod == null){
			missingValueTreatmentMethod = MissingValueTreatmentMethod.AS_IS;
		}

		switch(missingValueTreatmentMethod){
			case RETURN_INVALID:
				if(missingValueReplacement != null){
					report(new MisplacedAttributeException(miningField, PMMLAttributes.MININGFIELD_MISSINGVALUEREPLACEMENT, missingValueReplacement));
				}
				break;
			default:
				break;
		}

		Number lowValue = miningField.getLowValue();
		Number highValue = miningField.getHighValue();

		if((lowValue != null && highValue != null) && Double.compare(lowValue.doubleValue(), highValue.doubleValue()) > 0){
			throw new InvalidElementException(miningField);
		}

		return super.visit(miningField);
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
	public VisitorAction visit(OutputField outputField){
		ResultFeature resultFeature = outputField.getResultFeature();
		String targetFieldName = outputField.getTargetField();

		if(targetFieldName != null){

			switch(resultFeature){
				case TRANSFORMED_VALUE:
				case DECISION:
					Expression expression = outputField.getExpression();
					if(expression != null){
						report(new MisplacedElementException(expression));
					}
					// Falls through
				case WARNING:
					report(new MisplacedAttributeException(outputField, PMMLAttributes.OUTPUTFIELD_TARGETFIELD, targetFieldName));
					break;
				default:
					break;
			}
		}

		return super.visit(outputField);
	}

	@Override
	public VisitorAction visit(SimplePredicate simplePredicate){
		SimplePredicate.Operator operator = simplePredicate.getOperator();

		if(operator != null){

			switch(operator){
				case IS_MISSING:
				case IS_NOT_MISSING:
					if(simplePredicate.hasValue()){
						report(new MisplacedAttributeException(simplePredicate, PMMLAttributes.SIMPLEPREDICATE_VALUE, simplePredicate.getValue()));
					}
					break;
				default:
					break;
			}
		}

		return super.visit(simplePredicate);
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
	public VisitorAction visit(TargetValue targetValue){
		Number defaultValue = targetValue.getDefaultValue();
		Object value = targetValue.getValue();
		Number priorProbability = targetValue.getPriorProbability();

		if(defaultValue != null){

			if(value != null || priorProbability != null){
				report(new InvalidElementException(targetValue));
			}
		}

		return super.visit(targetValue);
	}

	@Override
	public VisitorAction visit(TextIndexNormalization textIndexNormalization){
		String wordRE = textIndexNormalization.getWordRE();
		String wordSeparatorCharacterRE = textIndexNormalization.getWordSeparatorCharacterRE();

		if(wordRE != null && wordSeparatorCharacterRE != null){
			report(new InvalidElementException(textIndexNormalization));
		}

		return super.visit(textIndexNormalization);
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
