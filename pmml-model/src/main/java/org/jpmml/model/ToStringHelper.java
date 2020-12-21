/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

public class ToStringHelper {

	private String prefix = null;

	private String delimiter = null;

	private String suffix = null;

	private StringBuilder value = null;


	public ToStringHelper(Object object){
		this.prefix = (object.getClass()).getSimpleName() + "{";
		this.delimiter = ", ";
		this.suffix = "}";
	}

	public ToStringHelper add(String key, Object value){

		if(this.value == null){
			this.value = new StringBuilder();
		} else

		{
			this.value.append(this.delimiter);
		}

		this.value.append(key).append('=').append(value);

		return this;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append(this.prefix);

		if(this.value != null){
			sb.append(this.value);
		}

		sb.append(this.suffix);

		return sb.toString();
	}
}
