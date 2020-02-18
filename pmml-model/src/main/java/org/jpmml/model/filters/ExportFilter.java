/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.dmg.pmml.Version;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * <p>
 * A SAX filter that translates PMML schema version 4.4 documents to PMML schema version 3.X and 4.X documents.
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

				attributes = renameAttribute(attributes, "defaultValue", "mapMissingTo");
			}
		} else

		if(("PMML").equals(localName)){
			Version target = getTarget();

			if(hasAttribute(attributes, "x-baseVersion")){
				attributes = removeAttribute(attributes, "x-baseVersion");
			}

			attributes = setAttribute(attributes, "version", target.getVersion());
		} else

		if(("TargetValue").equals(localName)){

			if(compare(getTarget(), Version.PMML_3_1) <= 0 && hasAttribute(attributes, "displayValue")){
				attributes = renameAttribute(attributes, "displayValue", "rawDataValue");
			}
		}

		return attributes;
	}
}