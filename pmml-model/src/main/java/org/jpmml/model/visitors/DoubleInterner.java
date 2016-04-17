/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.RealSparseArray;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that interns {@link Double} attribute values.
 * </p>
 */
public class DoubleInterner extends NumberInterner<Double> {

	public DoubleInterner(){
		super(DoubleInterner.cache);
	}

	@Override
	public Double canonicalize(Double value){
		return value;
	}

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof Double){
				Double number = (Double)value;

				ReflectionUtil.setFieldValue(field, object, intern(number));
			}
		}

		return VisitorAction.CONTINUE;
	}

	@Override
	public VisitorAction visit(RealSparseArray realSparseArray){

		if(realSparseArray.hasEntries()){
			internAll(realSparseArray.getEntries());
		}

		return super.visit(realSparseArray);
	}

	static
	public void clear(){
		DoubleInterner.cache.clear();
	}

	private static final ConcurrentMap<Double, Double> cache = new ConcurrentHashMap<>();
}