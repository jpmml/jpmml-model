/*
 * Copyright (c) 2012 University of Tartu
 */
package org.dmg.pmml;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class IOUtil {

	private IOUtil(){
	}

	static
	public Source createImportSource(InputSource source) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		ImportFilter filter = new ImportFilter(reader);

		return new SAXSource(filter, source);
	}

	static
	public PMML unmarshal(File file) throws IOException, SAXException, JAXBException {
		InputStream is = new FileInputStream(file);

		try {
			return unmarshal(is);
		} finally {
			is.close();
		}
	}

	static
	public PMML unmarshal(InputStream is) throws SAXException, JAXBException {
		return unmarshal(new InputSource(is));
	}

	static
	public PMML unmarshal(InputSource source) throws SAXException, JAXBException {
		return (PMML)unmarshal(createImportSource(source));
	}

	static
	public Object unmarshal(Source source) throws JAXBException {
		Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();

		return unmarshaller.unmarshal(source);
	}

	static
	public void marshal(PMML pmml, File file) throws IOException, JAXBException {
		OutputStream os = new FileOutputStream(file);

		try {
			marshal(pmml, os);
		} finally {
			os.close();
		}
	}

	static
	public void marshal(PMML pmml, OutputStream os) throws JAXBException {
		marshal(pmml, new StreamResult(os));
	}

	static
	public void marshal(PMML pmml, Result result) throws JAXBException {
		Marshaller marshaller = getJAXBContext().createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		marshaller.marshal(pmml, result);
	}

	static
	private JAXBContext getJAXBContext() throws JAXBException {

		if(IOUtil.jaxbCtx == null){
			IOUtil.jaxbCtx = JAXBContext.newInstance(ObjectFactory.class);
		}

		return IOUtil.jaxbCtx;
	}

	private static JAXBContext jaxbCtx = null;
}