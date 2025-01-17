/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model.moxy;

import jakarta.xml.bind.JAXBException;
import org.eclipse.persistence.jaxb.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBMarshaller;
import org.eclipse.persistence.jaxb.JAXBUnmarshaller;
import org.jpmml.model.JAXBSerializer;

public class MOXyJAXBSerializer extends JAXBSerializer {

	public MOXyJAXBSerializer() throws JAXBException {
		this(MOXyJAXBUtil.getContext());
	}

	public MOXyJAXBSerializer(JAXBContext jaxbContext){
		super(jaxbContext);
	}

	@Override
	protected JAXBMarshaller createMarshaller() throws JAXBException {
		return (JAXBMarshaller)super.createMarshaller();
	}

	@Override
	protected JAXBUnmarshaller createUnmarshaller() throws JAXBException {
		return (JAXBUnmarshaller)super.createUnmarshaller();
	}

	@Override
	protected JAXBContext getJAXBContext(){
		return (JAXBContext)super.getJAXBContext();
	}
}