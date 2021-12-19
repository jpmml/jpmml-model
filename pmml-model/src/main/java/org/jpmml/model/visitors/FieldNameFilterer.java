/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.ResultFeature;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * This class provides a skeletal implementation of {@link Field#getName() field name} filterers.
 * </p>
 */
abstract
public class FieldNameFilterer extends AbstractVisitor {

	abstract
	public String filter(String name);

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getFields(object.getClass());

		for(Field field : fields){

			if(ReflectionUtil.isFieldName(field)){
				String name = (String)ReflectionUtil.getFieldValue(field, object);

				name = filter(name);

				ReflectionUtil.setFieldValue(field, object, name);
			}
		}

		return super.visit(object);
	}

	@Override
	public VisitorAction visit(OutputField outputField){
		ResultFeature resultFeature = outputField.getResultFeature();

		switch(resultFeature){
			case TRANSFORMED_VALUE:
			case DECISION:
				{
					String segmentId = outputField.getSegmentId();

					if(segmentId != null){
						Object value = outputField.getValue();

						if(value instanceof String){
							value = filter((String)value);
						}

						outputField.setValue(value);
					}
				}
				break;
			default:
				break;
		}

		return super.visit(outputField);
	}
}