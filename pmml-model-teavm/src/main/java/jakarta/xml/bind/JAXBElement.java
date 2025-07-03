/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package jakarta.xml.bind;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class JAXBElement<T> implements Serializable {

	protected QName name = null;

	protected Class<T> declaredType = null;

	protected T value = null;


	public JAXBElement(QName name, Class<T> declaredType, T value){
		this.name = name;
		this.declaredType = declaredType;
		this.value = value;
	}

	public QName getName(){
		return this.name;
	}

	public Class<T> getDeclaredType(){
		return this.declaredType;
	}

	public T getValue(){
		return this.value;
	}

	public void setValue(T value){
		this.value = value;
	}
}