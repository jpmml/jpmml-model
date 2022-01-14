/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import org.dmg.pmml.Interval;
import org.dmg.pmml.InvalidValueTreatmentMethod;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.MissingElementException;
import org.jpmml.model.MissingMarkupException;
import org.jpmml.model.ReflectionUtil;

public class MissingMarkupInspector extends MarkupInspector<MissingMarkupException> {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof List){
				List<?> collection = (List<?>)value;

				// The getter method may have initialized the field with an empty ArrayList instance
				if(collection.isEmpty()){
					value = null;
				}
			} // End if

			// The field is set
			if(value != null){
				continue;
			}

			XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);
			if(attribute != null && attribute.required()){
				report(new MissingAttributeException(object, field));
			}

			XmlElement element = field.getAnnotation(XmlElement.class);
			if(element != null && element.required()){
				report(new MissingElementException(object, field));
			}
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(Interval interval){
		Number leftMargin = interval.getLeftMargin();
		Number rightMargin = interval.getRightMargin();

		if(leftMargin == null && rightMargin == null){
			report(new MissingAttributeException(interval, PMMLAttributes.INTERVAL_LEFTMARGIN));
			report(new MissingAttributeException(interval, PMMLAttributes.INTERVAL_RIGHTMARGIN));
		}

		return super.visit(interval);
	}

	@Override
	public VisitorAction visit(MiningField miningField){
		InvalidValueTreatmentMethod invalidValueTreatmentMethod = miningField.getInvalidValueTreatment();
		Object invalidValueReplacement = miningField.getInvalidValueReplacement();

		switch(invalidValueTreatmentMethod){
			case AS_VALUE:
				if(invalidValueReplacement == null){
					report(new MissingAttributeException(miningField, PMMLAttributes.MININGFIELD_INVALIDVALUEREPLACEMENT));
				}
				break;
			default:
				break;
		}

		return super.visit(miningField);
	}

	@Override
	public VisitorAction visit(SimplePredicate simplePredicate){
		SimplePredicate.Operator operator = simplePredicate.getOperator();

		if(operator != null){

			switch(operator){
				case EQUAL:
				case NOT_EQUAL:
				case LESS_THAN:
				case LESS_OR_EQUAL:
				case GREATER_OR_EQUAL:
				case GREATER_THAN:
					if(!simplePredicate.hasValue()){
						report(new MissingAttributeException(simplePredicate, PMMLAttributes.SIMPLEPREDICATE_VALUE));
					}
					break;
				default:
					break;
			}
		}

		return super.visit(simplePredicate);
	}
}