/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import org.dmg.pmml.FeatureType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * This class provides a skeletal implementation of {@link FieldName} filterers.
 * </p>
 */
abstract
public class FieldNameFilterer extends AbstractSimpleVisitor {

	abstract
	public FieldName filter(FieldName name);

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getAllInstanceFields(object);

		for(Field field : fields){

			if((FieldName.class).equals(field.getType())){
				FieldName name = (FieldName)ReflectionUtil.getFieldValue(field, object);

				name = filter(name);

				ReflectionUtil.setFieldValue(field, object, name);
			}
		}

		return VisitorAction.CONTINUE;
	}

	@Override
	public VisitorAction visit(OutputField outputField){
		FeatureType feature = outputField.getFeature();

		switch(feature){
			case TRANSFORMED_VALUE:
			case DECISION:
				{
					String segmentId = outputField.getSegmentId();

					if(segmentId != null){
						outputField.setValue(filter(outputField.getValue()));
					}
				}
				break;
			default:
				break;
		}

		return super.visit(outputField);
	}

	private String filter(String value){
		FieldName name = (value != null ? FieldName.create(value) : null);

		name = filter(name);

		return (name != null ? name.getValue() : null);
	}
}