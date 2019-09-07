/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.dmg.pmml.PMMLObject;

public class ServiceLoaderUtil {

	private ServiceLoaderUtil(){
	}

	static
	public <E extends PMMLObject> E load(Class<E> clazz, ClassLoader clazzLoader){
		ServiceLoader<E> serviceLoader;

		try {
			serviceLoader = ServiceLoader.load(clazz, clazzLoader);
		} catch(ServiceConfigurationError sce){
			throw new IllegalArgumentException(sce);
		}

		Iterator<E> it = serviceLoader.iterator();

		if(it.hasNext()){
			E object = it.next();

			if(it.hasNext()){
				throw new IllegalArgumentException();
			}

			return object;
		}

		throw new IllegalArgumentException();
	}
}