/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dmg.pmml.IntSparseArray;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.RealSparseArray;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that interns {@link Integer} attribute values.
 * </p>
 */
public class IntegerInterner extends NumberInterner<Integer> {

	public IntegerInterner(){
		super(IntegerInterner.cache);
	}

	@Override
	public Integer canonicalize(Integer value){
		return Integer.valueOf(value.intValue());
	}

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof Integer){
				Integer number = (Integer)value;

				ReflectionUtil.setFieldValue(field, object, intern(number));
			}
		}

		return VisitorAction.CONTINUE;
	}

	@Override
	public VisitorAction visit(IntSparseArray intSparseArray){

		if(intSparseArray.hasEntries()){
			internAll(intSparseArray.getEntries());
		} // End if

		if(intSparseArray.hasIndices()){
			internAll(intSparseArray.getIndices());
		}

		return super.visit(intSparseArray);
	}

	@Override
	public VisitorAction visit(RealSparseArray realSparseArray){

		if(realSparseArray.hasIndices()){
			internAll(realSparseArray.getIndices());
		}

		return super.visit(realSparseArray);
	}

	static
	public void clear(){
		IntegerInterner.cache.clear();
	}

	private static final ConcurrentMap<Integer, Integer> cache = new ConcurrentHashMap<>();
}