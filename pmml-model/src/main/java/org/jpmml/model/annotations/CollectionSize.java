/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.FIELD}
)
public @interface CollectionSize {

	String value();

	Operator operator() default Operator.EQUAL;

	static
	public enum Operator {
		EQUAL(){

			@Override
			public boolean check(int size, Collection<?> collection){
				return size == collection.size();
			}
		},
		GREATER_OR_EQUAL(){

			@Override
			public boolean check(int size, Collection<?> collection){
				return size >= collection.size();
			}
		}
		;

		abstract
		public boolean check(int size, Collection<?> collection);
	}
}