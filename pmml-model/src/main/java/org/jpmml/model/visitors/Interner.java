/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ListIterator;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

abstract
public class Interner<V> extends AbstractSimpleVisitor {

	private Class<? extends V> type = null;


	public Interner(Class<? extends V> type){
		setType(type);
	}

	abstract
	public V intern(V value);

	public void internAll(List<V> values){

		for(ListIterator<V> it = values.listIterator(); it.hasNext(); ){
			it.set(intern(it.next()));
		}
	}

	@Override
	public VisitorAction visit(PMMLObject object){
		Class<? extends V> type = getType();

		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());
		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(type.isInstance(value)){
				V internedValue = intern(type.cast(value));

				ReflectionUtil.setFieldValue(field, object, internedValue);
			}
		}

		return VisitorAction.CONTINUE;
	}

	public Class<? extends V> getType(){
		return this.type;
	}

	private void setType(Class<? extends V> type){
		this.type = type;
	}
}