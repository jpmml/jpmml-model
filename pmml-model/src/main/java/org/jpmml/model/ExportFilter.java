/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.transform.sax.*;

import org.jpmml.schema.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * SAX filter for translating PMML schema version 4.2 documents to PMML schema version 3.X and 4.X documents.
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
	 * @param source An {@link InputSource} that contains PMML schema version 4.2 document.
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