/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonAutoDetect (
	fieldVisibility = JsonAutoDetect.Visibility.ANY,
	getterVisibility = JsonAutoDetect.Visibility.NONE,
	isGetterVisibility = JsonAutoDetect.Visibility.NONE,
	setterVisibility = JsonAutoDetect.Visibility.NONE
)
final
public class FieldName implements Serializable {

	@JsonValue
	private String value = null;


	private FieldName(String value){
		setValue(value);
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

	/**
	 * Replaces this newly deserialized {@link FieldName} instance with the singleton one.
	 *
	 * @see Serializable
	 */
	final
	Object readResolve(){
		return create(getValue());
	}

	@JsonCreator
	static
	public FieldName create(String value){

		if(value == null){
			throw new NullPointerException();
		} else

		if(("").equals(value)){
			throw new IllegalArgumentException("Name cannot be empty");
		}

		Map<String, FieldName> cache = FieldName.CACHE_PROVIDER.get();

		FieldName name = cache.get(value);
		if(name == null){
			name = new FieldName(value);

			FieldName prevName = cache.putIfAbsent(value, name);
			if(prevName != null){
				return prevName;
			}
		}

		return name;
	}

	public static final Map<String, FieldName> CACHE = new ConcurrentHashMap<>(2048);

	public static final ThreadLocal<Map<String, FieldName>> CACHE_PROVIDER = ThreadLocal.withInitial(new Supplier<Map<String, FieldName>>(){

		@Override
		public Map<String, FieldName> get(){
			return FieldName.CACHE;
		}
	});
}