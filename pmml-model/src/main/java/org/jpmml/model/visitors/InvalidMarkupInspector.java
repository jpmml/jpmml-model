/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.dmg.pmml.Apply;
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
import org.jpmml.model.InvalidAttributeException;
import org.jpmml.model.InvalidElementException;
import org.jpmml.model.InvalidMarkupException;
import org.jpmml.model.MisplacedAttributeException;
import org.jpmml.model.MisplacedElementException;
import org.jpmml.model.ReflectionUtil;
import org.jpmml.model.annotations.CollectionSize;

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
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getFields(object.getClass());

		for(Field field : fields){
			CollectionSize collectionSize = field.getAnnotation(CollectionSize.class);

			if(collectionSize != null){
				Field collectionField = ReflectionUtil.getField(object.getClass(), collectionSize.value());

				Object sizeValue = ReflectionUtil.getFieldValue(field, object);
				Object collectionValue = ReflectionUtil.getFieldValue(collectionField, object);

				if(sizeValue != null){
					int size = (Integer)sizeValue;
					List<?> collection = (List<?>)collectionValue;

					boolean valid;

					CollectionSize.Operator operator = collectionSize.operator();
					switch(operator){
						case EQUAL:
						case GREATER_OR_EQUAL:
							valid = operator.check(size, (collection != null ? collection : Collections.emptyList()));
							break;
						default:
							valid = true;
							break;
					}

					if(!valid){
						report(new InvalidAttributeException(object, field, sizeValue));
					}
				}
			}
		}

		return super.visit(object);
	}

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
	public VisitorAction visit(Interval interval){
		Number leftMargin = interval.getLeftMargin();
		Number rightMargin = interval.getRightMargin();

		if((leftMargin != null && rightMargin != null) && Double.compare(leftMargin.doubleValue(), rightMargin.doubleValue()) > 0){
			report(new InvalidElementException(interval));
		}

		return super.visit(interval);
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
}
