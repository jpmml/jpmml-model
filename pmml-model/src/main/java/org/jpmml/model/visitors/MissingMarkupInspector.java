/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import org.dmg.pmml.PMMLObject;
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

			XmlElement element = field.getAnnotation(XmlElement.class);
			if(element != null && element.required()){
				report(new MissingElementException(object, field));
			}

			XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);
			if(attribute != null && attribute.required()){
				report(new MissingAttributeException(object, field));
			}
		}

		return super.visit(object);
	}
}