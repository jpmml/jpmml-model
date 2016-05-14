/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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

			return (cachedName == this);
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

	/**
	 * Replaces this newly deserialized {@link FieldName} instance with the singleton one.
	 *
	 * @see Serializable
	 */
	final
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

		FieldName.cache.put(value, new WeakReference<>(name));

		return name;
	}

	static
	public void compact(){
		FieldName.cache.compact();
	}

	private static final Cache cache = new Cache();

	static
	private class Cache extends ConcurrentHashMap<String, WeakReference<FieldName>> {

		private AtomicLong counter = new AtomicLong();


		@Override
		public WeakReference<FieldName> put(String key, WeakReference<FieldName> value){
			WeakReference<FieldName> result = super.put(key, value);

			// Perform cache compaction after every 100 put operations
			if((this.counter.incrementAndGet() % 100L) == 0L){
				compact();
			}

			return result;
		}

		private void compact(){
			Collection<WeakReference<FieldName>> references = values();

			for(Iterator<WeakReference<FieldName>> it = references.iterator(); it.hasNext(); ){
				WeakReference<FieldName> reference = it.next();

				FieldName cachedName = reference.get();
				if(cachedName == null){
					it.remove();
				}
			}
		}
	}
}