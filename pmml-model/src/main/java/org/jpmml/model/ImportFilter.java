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

/**
 * <p>
 * A SAX filter that translates PMML schema version 3.X and 4.X documents to PMML schema version 4.3 documents.
 * </p>
 */
public class ImportFilter extends PMMLFilter {

	private boolean extensions = true;


	public ImportFilter(){
		this(true);
	}

	public ImportFilter(boolean extensions){
		super(Version.PMML_4_3);

		setExtensions(extensions);
	}

	public ImportFilter(XMLReader reader){
		this(reader, true);
	}

	public ImportFilter(XMLReader reader, boolean extensions){
		super(reader, Version.PMML_4_3);

		setExtensions(extensions);
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

			if(this.extensions){
				attributes = renameAttribute(attributes, "version", "x-baseVersion");
			}

			return setAttribute(attributes, "version", target.getVersion());
		} else

		if(("TargetValue").equals(localName) && compare(getSource(), Version.PMML_3_1) <= 0){
			return renameAttribute(attributes, "rawDataValue", "displayValue");
		}

		return attributes;
	}

	public boolean getExtensions(){
		return this.extensions;
	}

	private void setExtensions(boolean extensions){
		this.extensions = extensions;
	}

	/**
	 * @param source An {@link InputSource} that contains PMML schema version 3.X or 4.X document.
	 *
	 * @return A {@link SAXSource} that contains PMML schema version 4.3 document.
	 */
	static
	public SAXSource apply(InputSource source) throws SAXException {
		return JAXBUtil.createFilteredSource(source, new ImportFilter());
	}
}
