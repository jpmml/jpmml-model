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
 * If working with large class model objects, then it is advisable (for maximum performance) to configure the size of the string pool beforehand using JVM options <code>-XX:MaxPermSize</code> (Java 1.6) or <code>-XX:StringTableSize</code> (Java 1.7 and newer).
 * For more information, see <a href="http://java-performance.info/string-intern-in-java-6-7-8/">String.intern in Java 6, 7 and 8 – string pooling</a>.
 * </p>
 */
public class StringInterner extends Interner<String> {

	public StringInterner(){
		super(String.class);
	}

	@Override
	public String intern(String string){
		return string.intern();
	}
}