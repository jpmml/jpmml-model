/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

final
public class FieldName implements Serializable {

	private String value = null;


	public FieldName(String value){
		setValue(value);
	}

	public boolean isInterned(){
		WeakReference<FieldName> reference = FieldName.cache.get(getValue());

		if(reference != null){
			FieldName cachedName = reference.get();

			if(cachedName != null){
				return (cachedName == this);
			}
		}

		return false;
	}

	public FieldName intern(){
		return create(getValue());
	}

	@Override
	public int hashCode(){
		return (this.getValue()).hashCode();
	}

	@Override
	public boolean equals(Object object){

		if(object == this){
			return true;
		} // End if

		if(object instanceof FieldName){
			FieldName that = (FieldName)object;

			return (this.getValue()).equals(that.getValue());
		}

		return false;
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

		if(value == null || ("").equals(value)){
			throw new IllegalArgumentException();
		}

		WeakReference<FieldName> reference = FieldName.cache.get(value);
		if(reference != null){
			FieldName cachedName = reference.get();

			if(cachedName != null){
				return cachedName;
			}
		}

		FieldName name = new FieldName(value);

		FieldName.cache.put(value, new WeakReference<FieldName>(name));

		return name;
	}

	private static final Map<String, WeakReference<FieldName>> cache = new WeakHashMap<String, WeakReference<FieldName>>();
}