/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Objects;

import org.dmg.pmml.PMMLObject;

public class PMMLObjectKey {

	private PMMLObject object = null;


	public PMMLObjectKey(PMMLObject object){
		setObject(object);
	}

	@Override
	public int hashCode(){
		return ReflectionUtil.hashCode(getObject());
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof PMMLObjectKey){
			PMMLObjectKey that = (PMMLObjectKey)object;

			return ReflectionUtil.equals(this.getObject(), that.getObject());
		}

		return false;
	}

	public PMMLObject getObject(){
		return this.object;
	}

	private void setObject(PMMLObject object){
		this.object = Objects.requireNonNull(object);
	}
}