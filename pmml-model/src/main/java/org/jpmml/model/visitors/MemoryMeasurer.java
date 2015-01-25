/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;
import org.jpmml.agent.InstrumentationProvider;
import org.jpmml.model.ReflectionUtil;

public class MemoryMeasurer extends AbstractSimpleVisitor {

	private Instrumentation instrumentation = InstrumentationProvider.getInstrumentation();

	private Map<Object, Long> objectSizes = new IdentityHashMap<Object, Long>(1024 * 1024);


	@Override
	public VisitorAction visit(PMMLObject object){
		measure(object);

		return VisitorAction.CONTINUE;
	}

	@Override
	public void applyTo(Visitable visitable){
		reset();

		super.applyTo(visitable);
	}

	public void reset(){
		this.objectSizes.clear();
	}

	public int getObjectCount(){
		return this.objectSizes.size();
	}

	public long getSize(){
		long sum = 0L;

		Collection<Long> sizes = this.objectSizes.values();
		for(Long size : sizes){
			sum += size.longValue();
		}

		return sum;
	}

	private void measure(Object object){
		boolean status = register(object);

		if(!status){
			return;
		} // End if

		// Wrapper objects for primitive values do not have Object-type instance fields
		if(ReflectionUtil.isPrimitiveWrapper(object)){
			return;
		}

		List<Field> fields = ReflectionUtil.getAllInstanceFields(object);
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

	private boolean register(Object object){

		if(this.objectSizes.containsKey(object)){
			return false;
		}

		Long size = Long.valueOf(this.instrumentation.getObjectSize(object));

		this.objectSizes.put(object, size);

		return true;
	}

	static
	private boolean shouldMeasure(Object object){

		if(object != null){

			if(ReflectionUtil.isEnum(object)){
				return false;
			}

			return !(object instanceof Visitable);
		}

		return false;
	}
}