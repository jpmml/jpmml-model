/*
 * Copyright (c) 2009 University of Tartu
 */
package org.jpmml.model;

import javax.xml.transform.sax.SAXSource;

import org.jpmml.schema.Version;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>
 * A SAX filter that translates PMML schema version 3.X and 4.X documents to PMML schema version 4.2 documents.
 * </p>
 */
public class ImportFilter extends PMMLFilter {

	public ImportFilter(){
		super(Version.PMML_4_2);
	}

	public ImportFilter(XMLReader reader){
		super(reader, Version.PMML_4_2);
	}

	@Override
	public String filterLocalName(String localName){

		if(("Trend").equals(localName) && compare(getSource(), Version.PMML_4_0) == 0){
			return "Trend_ExpoSmooth";
		}

		return localName;
	}

	@Override
	public Attributes filterAttributes(String localName, Attributes attributes){

		if(("Apply").equals(localName) && compare(getSource(), Version.PMML_4_1) == 0){
			return renameAttribute(attributes, "mapMissingTo", "defaultValue");
		} else

		if(("PMML").equals(localName)){
			Version target = getTarget();

			return setAttribute(attributes, "version", target.getVersion());
		} else

		if(("TargetValue").equals(localName) && compare(getSource(), Version.PMML_3_1) <= 0){
			return renameAttribute(attributes, "rawDataValue", "displayValue");
		}

		return attributes;
	}

	/**
	 * @param source An {@link InputSource} that contains PMML schema version 3.X or 4.X document.
	 *
	 * @return A {@link SAXSource} that contains PMML schema version 4.2 document.
	 */
	static
	public SAXSource apply(InputSource source) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		ImportFilter filter = new ImportFilter(reader);

		return new SAXSource(filter, source);
	}
}