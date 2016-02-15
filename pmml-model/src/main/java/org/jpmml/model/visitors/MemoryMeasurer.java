/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;
import org.jpmml.agent.InstrumentationProvider;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that measures the deep size of a class model object.
 * </p>
 *
 * <p>
 * The object size is measured using {@link Instrumentation#getObjectSize(Object)} method.
 * </p>
 */
public class MemoryMeasurer extends AbstractSimpleVisitor {

	private Instrumentation instrumentation = InstrumentationProvider.getInstrumentation();

	private long size = 0L;

	private Set<Object> objects = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());


	/**
	 * @throws IllegalStateException If the JPMML agent is not available.
	 */
	public MemoryMeasurer(){
	}

	@Override
	public void applyTo(Visitable visitable){
		reset();

		super.applyTo(visitable);
	}

	@Override
	public VisitorAction visit(PMMLObject object){
		measure(object);

		return VisitorAction.CONTINUE;
	}

	public void reset(){
		this.size = 0L;

		this.objects.clear();
	}

	public long getSize(){
		return this.size;
	}

	public Set<Object> getObjects(){
		return this.objects;
	}

	private void measure(Object object){
		boolean status = this.objects.add(object);

		if(!status){
			return;
		}

		this.size += this.instrumentation.getObjectSize(object);

		Class<?> clazz = object.getClass();

		// Wrapper objects for primitive values do not have Object-type instance fields
		if(ReflectionUtil.isPrimitiveWrapper(clazz)){
			return;
		}

		List<Field> fields = ReflectionUtil.getInstanceFields(clazz);
		for(Field field : fields){
			Class<?> type = field.getType();

			if(type.isPrimitive()){
				continue;
			}

			Object value = ReflectionUtil.getFieldValue(field, object);
			if(shouldMeasure(value)){
				measure(value);
			}
		}

		if(object instanceof Object[]){
			Object[] values = (Object[])object;

			for(int i = 0; i < values.length; i++){
				Object value = values[i];

				if(shouldMeasure(value)){
					measure(value);
				}
			}
		}
	}

	static
	private boolean shouldMeasure(Object object){

		if(object != null){
			Class<?> clazz = object.getClass();

			if(clazz.isEnum()){
				return false;
			} // End if

			if(object instanceof FieldName){
				FieldName name = (FieldName)object;

				return !name.isInterned();
			}

			return !(object instanceof Visitable);
		}

		return false;
	}
}