/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;

import org.dmg.pmml.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class SchemaUtil {

	private SchemaUtil(){
	}

	/**
	 * Transforms older PMML schema version documents to the newest PMML schema version one.
	 *
	 * @param source Any PMML schema version 3.X or 4.X document
	 *
	 * @return A PMML schema version 4.1 document
	 *
	 * @see ImportFilter
	 */
	static
	public Source createImportSource(InputSource source) throws SAXException {
		XMLReader reader = XMLReaderFactory.createXMLReader();

		ImportFilter filter = new ImportFilter(reader);

		return new SAXSource(filter, source);
	}
}