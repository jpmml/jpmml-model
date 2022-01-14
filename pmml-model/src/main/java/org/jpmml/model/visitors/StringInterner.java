/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

/**
 * <p>
 * A Visitor that interns {@link String} attribute values.
 * </p>
 *
 * <p>
 * Strings are interned using the standard {@link String#intern()} method.
 * </p>
 */
public class StringInterner extends AttributeInterner<String> {

	public StringInterner(){
		super(String.class);
	}

	@Override
	public String intern(String string){
		return string.intern();
	}
}