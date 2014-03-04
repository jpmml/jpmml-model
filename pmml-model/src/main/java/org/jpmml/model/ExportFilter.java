/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.transform.sax.*;

import org.dmg.pmml.*;

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
	public String filterLocalName(String name){

		if("Trend_ExpoSmooth".equals(name)){

			if(compare(getTarget(), Version.PMML_4_0) == 0){
				return "Trend";
			}
		}

		return super.filterLocalName(name);
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