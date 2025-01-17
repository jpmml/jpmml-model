/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model.metro;

import java.io.OutputStream;

import jakarta.xml.bind.JAXBException;
import org.dmg.pmml.PMMLObject;
import org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl;
import org.glassfish.jaxb.runtime.v2.runtime.MarshallerImpl;
import org.glassfish.jaxb.runtime.v2.runtime.output.XmlOutput;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.UnmarshallerImpl;
import org.jpmml.model.JAXBSerializer;

public class MetroJAXBSerializer extends JAXBSerializer {

	public MetroJAXBSerializer() throws JAXBException {
		this(MetroJAXBUtil.getContext());
	}

	public MetroJAXBSerializer(JAXBContextImpl jaxbContext){
		super(jaxbContext);
	}

	@Override
	public void serialize(PMMLObject object, OutputStream os) throws JAXBException {
		MarshallerImpl marshaller = createMarshaller();

		XmlOutput xmlOutput = new PrettyUTF8XmlOutput(os, marshaller.getContext());

		marshaller.marshal(object, xmlOutput);
	}

	@Override
	protected MarshallerImpl createMarshaller() throws JAXBException {
		return (MarshallerImpl)super.createMarshaller();
	}

	@Override
	public UnmarshallerImpl createUnmarshaller() throws JAXBException {
		return (UnmarshallerImpl)super.createUnmarshaller();
	}

	@Override
	protected JAXBContextImpl getJAXBContext(){
		return (JAXBContextImpl)super.getJAXBContext();
	}
}