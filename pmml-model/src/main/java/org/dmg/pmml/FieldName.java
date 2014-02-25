/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.*;
import java.lang.ref.*;
import java.util.*;

final
public class FieldName implements Serializable {

	private String value = null;


	public FieldName(String value){
		setValue(value);
	}

	public FieldName intern(){
		return create(getValue());
	}

	@Override
	public int hashCode(){
		return getValue().hashCode();
	}

	@Override
	public boolean equals(Object object){

		if(object instanceof FieldName){
			FieldName that = (FieldName)object;

			return (this.getValue()).equals(that.getValue());
		}

		return super.equals(object);
	}

	@Override
	public String toString(){
		return getValue();
	}

	public String getValue(){
		return this.value;
	}

	private void setValue(String value){

		if(value == null){
			throw new NullPointerException();
		}

		this.value = value;
	}

	Object readResolve(){
		return intern();
	}

	static
	public FieldName create(String value){
		return unmarshal(value);
	}

	static
	FieldName unmarshal(String value){
		FieldName name = new FieldName(value);

		WeakReference<FieldName> reference = FieldName.cache.get(name);
		if(reference != null){
			FieldName cachedName = reference.get();
			if(cachedName != null){
				return cachedName;
			}
		}

		FieldName.cache.put(name, new WeakReference<FieldName>(name));

		return name;
	}

	static
	String marshal(FieldName name){

		// FieldName corresponds to a simple type in PMML XML Schema. Hence, it is possible to encounter a null instance.
		if(name == null){
			return null;
		}

		return name.getValue();
	}

	private static final Map<FieldName, WeakReference<FieldName>> cache = new WeakHashMap<FieldName, WeakReference<FieldName>>();
}