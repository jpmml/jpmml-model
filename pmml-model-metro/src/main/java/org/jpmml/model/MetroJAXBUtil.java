/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.MarshallerImpl;
import com.sun.xml.bind.v2.runtime.output.XmlOutput;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;

public class MetroJAXBUtil {

	private MetroJAXBUtil(){
	}

	static
	public void marshalPMML(PMML pmml, OutputStream os) throws JAXBException {
		marshal(pmml, os);
	}

	static
	public void marshal(PMMLObject object, OutputStream os) throws JAXBException {
		JAXBContextImpl context;

		try {
			context = (JAXBContextImpl)JAXBUtil.getContext();
		} catch(ClassCastException cce){
			throw new IllegalStateException("Not a GlassFish Metro runtime", cce);
		}

		MarshallerImpl marshaller = context.createMarshaller();

		XmlOutput xmlOutput = new PrettyUTF8XmlOutput(os, context.getUTF8NameTable());

		marshaller.marshal(object, xmlOutput);
	}
}