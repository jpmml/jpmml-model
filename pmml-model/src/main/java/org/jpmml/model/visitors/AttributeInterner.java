/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlList;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

abstract
public class AttributeInterner<V> extends Interner<V> {

	public AttributeInterner(Class<? extends V> type){
		super(type);
	}

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getFields(object.getClass());

		for(Field field : fields){
			XmlAttribute attribute = field.getAnnotation(XmlAttribute.class);
			XmlList list = field.getAnnotation(XmlList.class);

			if((attribute == null) && (list == null)){
				continue;
			}

			apply(field, object);
		}

		return super.visit(object);
	}
}