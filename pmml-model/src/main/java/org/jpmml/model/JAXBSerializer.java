/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.dmg.pmml.PMMLObject;

public class JAXBSerializer implements TextSerializer {

	private JAXBContext context = null;


	public JAXBSerializer() throws JAXBException {
		this(JAXBUtil.getContext());
	}

	public JAXBSerializer(JAXBContext context){
		setContext(context);
	}

	/**
	 * @see #unmarshal(Source)
	 */
	@Override
	public PMMLObject deserialize(InputStream is) throws JAXBException {
		return unmarshal(new StreamSource(is));
	}

	/**
	 * @see #marshal(PMMLObject, Result)
	 */
	@Override
	public void serialize(PMMLObject object, OutputStream os) throws JAXBException {
		marshal(object, new StreamResult(os));
	}

	/**
	 * @see #marshalPretty(PMMLObject, Result)
	 */
	@Override
	public void serializePretty(PMMLObject object, OutputStream os) throws JAXBException {
		marshalPretty(object, new StreamResult(os));
	}

	/**
	 * <p>
	 * Unmarshals a class model object.
	 * </p>
	 *
	 * @param source Input source containing a complete PMML schema version 4.4 document or any fragment of it.
	 */
	public PMMLObject unmarshal(Source source) throws JAXBException {
		Unmarshaller unmarshaller = createUnmarshaller();

		return (PMMLObject)unmarshaller.unmarshal(source);
	}

	/**
	 * <p>
	 * Marshals a class model object.
	 * </p>
	 */
	public void marshal(PMMLObject object, Result result) throws JAXBException {
		Marshaller marshaller = createMarshaller();

		marshaller.marshal(object, result);
	}

	/**
	 * <p>
	 * Marshals a class model object in a pretty way.
	 * </p>
	 */
	public void marshalPretty(PMMLObject object, Result result) throws JAXBException {
		Marshaller marshaller = createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		marshaller.marshal(object, result);
	}

	protected Marshaller createMarshaller() throws JAXBException {
		JAXBContext context = getContext();

		Marshaller marshaller = context.createMarshaller();

		return marshaller;
	}

	protected Unmarshaller createUnmarshaller() throws JAXBException {
		JAXBContext context = getContext();

		Unmarshaller unmarshaller = context.createUnmarshaller();

		return unmarshaller;
	}

	protected JAXBContext getContext(){
		return this.context;
	}

	private void setContext(JAXBContext context){
		this.context = Objects.requireNonNull(context);
	}
}