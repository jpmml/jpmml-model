/*
 * Copyright (c) 2014 Villu Ruusmann
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
 * A SAX filter that translates PMML schema version 4.3 documents to PMML schema version 3.X and 4.X documents.
 * </p>
 */
public class ExportFilter extends PMMLFilter {

	public ExportFilter(Version target){
		super(target);
	}

	public ExportFilter(XMLReader reader, Version target){
		super(reader, target);
	}

	@Override
	public String filterLocalName(String localName){

		if("Trend_ExpoSmooth".equals(localName)){

			if(compare(getTarget(), Version.PMML_4_0) == 0){
				return "Trend";
			}
		}

		return localName;
	}

	@Override
	public Attributes filterAttributes(String localName, Attributes attributes){

		if(("Apply").equals(localName)){

			if(compare(getTarget(), Version.PMML_4_1) == 0 && hasAttribute(attributes, "defaultValue")){

				if(hasAttribute(attributes, "mapMissingTo")){
					throw new IllegalStateException();
				}

				return renameAttribute(attributes, "defaultValue", "mapMissingTo");
			}
		} else

		if(("PMML").equals(localName)){
			Version target = getTarget();

			attributes = removeAttribute(attributes, "x-baseVersion");

			return setAttribute(attributes, "version", target.getVersion());
		} else

		if(("TargetValue").equals(localName)){

			if(compare(getTarget(), Version.PMML_3_1) <= 0 && hasAttribute(attributes, "displayValue")){
				return renameAttribute(attributes, "displayValue", "rawDataValue");
			}
		}

		return attributes;
	}

	/**
	 * @param source An {@link InputSource} that contains PMML schema version 4.3 document.
	 * @param version The target PMML schema version.
	 *
	 * @return A {@link SAXSource} containing the target PMML schema version document.
	 */
	static
	public SAXSource apply(InputSource source, Version version) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		ExportFilter filter = new ExportFilter(reader, version);

		return new SAXSource(filter, source);
	}
}