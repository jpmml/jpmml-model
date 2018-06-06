/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.StringJoiner;

public class ToStringHelper {

	private StringJoiner joiner = null;


	public ToStringHelper(Object object){
		setJoiner(new StringJoiner(", ", (object.getClass()).getSimpleName() + "{", "}"));
	}

	public ToStringHelper add(String key, Object value){
		StringJoiner joiner = getJoiner();

		joiner.add(key + "=" + value);

		return this;
	}

	@Override
	public String toString(){
		StringJoiner joiner = getJoiner();

		return joiner.toString();
	}

	public StringJoiner getJoiner(){
		return this.joiner;
	}

	private void setJoiner(StringJoiner joiner){
		this.joiner = joiner;
	}
}
