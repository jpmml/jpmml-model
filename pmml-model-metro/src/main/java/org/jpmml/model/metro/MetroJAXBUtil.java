/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import java.io.OutputStream;

import jakarta.xml.bind.JAXBException;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl;
import org.glassfish.jaxb.runtime.v2.runtime.MarshallerImpl;
import org.glassfish.jaxb.runtime.v2.runtime.output.XmlOutput;
import org.jpmml.model.JAXBUtil;

public class MetroJAXBUtil {

	private MetroJAXBUtil(){
	}

	static
	public void marshalPMML(PMML pmml, OutputStream os) throws JAXBException {
		marshal(pmml, os);
	}

	static
	public void marshal(PMMLObject object, OutputStream os) throws JAXBException {
		JAXBContextImpl context = getContext();

		MarshallerImpl marshaller = context.createMarshaller();

		XmlOutput xmlOutput = new PrettyUTF8XmlOutput(os, context.getUTF8NameTable());

		marshaller.marshal(object, xmlOutput);
	}

	static
	public JAXBContextImpl getContext() throws JAXBException {

		try {
			return (JAXBContextImpl)JAXBUtil.getContext();
		} catch(ClassCastException cce){
			throw new IllegalStateException("Not a GlassFish Metro runtime", cce);
		}
	}
}