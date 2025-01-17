/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl;
import org.jpmml.model.JAXBUtil;

public class MetroJAXBUtil {

	private MetroJAXBUtil(){
	}

	static
	public JAXBContextImpl getContext() throws JAXBException {

		if(MetroJAXBUtil.context == null){
			JAXBContext context = JAXBContext.newInstance(JAXBUtil.getObjectFactoryClasses());

			try {
				MetroJAXBUtil.context = (JAXBContextImpl)context;
			} catch(ClassCastException cce){
				throw new IllegalStateException("Not a GlassFish Metro runtime", cce);
			}
		}

		return MetroJAXBUtil.context;
	}

	private static JAXBContextImpl context = null;
}