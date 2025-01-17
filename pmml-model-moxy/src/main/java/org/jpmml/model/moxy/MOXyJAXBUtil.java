/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.moxy;

import jakarta.xml.bind.JAXBException;
import org.eclipse.persistence.jaxb.JAXBContext;
import org.jpmml.model.JAXBUtil;

public class MOXyJAXBUtil {

	private MOXyJAXBUtil(){
	}

	static
	public JAXBContext getContext() throws JAXBException {

		if(MOXyJAXBUtil.context == null){
			jakarta.xml.bind.JAXBContext context = jakarta.xml.bind.JAXBContext.newInstance(JAXBUtil.getObjectFactoryClasses());

			try {
				MOXyJAXBUtil.context = (JAXBContext)context;
			} catch(ClassCastException cce){
				throw new IllegalStateException("Not an EclipseLink MOXy runtime", cce);
			}
		}

		return MOXyJAXBUtil.context;
	}

	private static JAXBContext context = null;
}