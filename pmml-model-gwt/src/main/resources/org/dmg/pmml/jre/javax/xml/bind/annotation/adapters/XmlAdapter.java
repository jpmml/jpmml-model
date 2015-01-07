/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package javax.xml.bind.annotation.adapters;

abstract
public class XmlAdapter<V, B> {

	protected XmlAdapter(){
	}

	abstract
	public B unmarshal(V value) throws Exception;

	abstract
	public V marshal(B bound) throws Exception;
}