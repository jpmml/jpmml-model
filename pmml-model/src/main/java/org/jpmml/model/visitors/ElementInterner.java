/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

abstract
public class ElementInterner<V> extends Interner<V> {

	public ElementInterner(Class<? extends V> type){
		super(type);
	}

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getFields(object.getClass());

		for(Field field : fields){
			XmlElement element = field.getAnnotation(XmlElement.class);
			XmlElements elements = field.getAnnotation(XmlElements.class);

			if((element == null) && (elements == null)){
				continue;
			}

			apply(field, object);
		}

		return super.visit(object);
	}
}