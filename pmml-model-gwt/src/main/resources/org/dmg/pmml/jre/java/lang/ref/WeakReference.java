/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package java.lang.ref;

public class WeakReference<T> {

	private T referent = null;


	public WeakReference(T referent){
		this.referent = referent;
	}

	public T get(){
		return this.referent;
	}
}
