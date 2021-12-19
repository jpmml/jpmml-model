/*
 * Copyright (c) 2009 University of Tartu
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.dmg.pmml.Version;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

/**
 * <p>
 * A SAX filter that translates PMML schema version 3.X and 4.X documents to PMML schema version 4.4 documents.
 * </p>
 */
public class ImportFilter extends PMMLFilter {

	private boolean extensions = true;


	public ImportFilter(){
		this(true);
	}

	public ImportFilter(boolean extensions){
		super(Version.PMML_4_4);

		setExtensions(extensions);
	}

	public ImportFilter(XMLReader reader){
		this(reader, true);
	}

	public ImportFilter(XMLReader reader, boolean extensions){
		super(reader, Version.PMML_4_4);

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

		if(("Apply").equals(localName)){

			if(compare(getSource(), Version.PMML_4_1) == 0){
				attributes = renameAttribute(attributes, "mapMissingTo", "defaultValue");
			} // End if

			if(compare(getSource(), Version.PMML_4_3) <= 0){
				String function = getAttribute(attributes, "function");

				if(function != null){

					switch(function){
						case "x-acos":
						case "x-asin":
						case "x-atan":
						case "x-cos":
						case "x-cosh":
						case "x-expm1":
						case "x-hypot":
						case "x-ln1p":
						case "x-modulo":
						case "x-rint":
						case "x-sin":
						case "x-sinh":
						case "x-tan":
						case "x-tanh":
							{
								attributes = setAttribute(attributes, "function", function.substring("x-".length()));
							}
							break;
						default:
							break;
					}
				}
			}
		} else

		if(("MiningField").equals(localName)){

			if(compare(getSource(), Version.PMML_4_3) <= 0){
				attributes = renameAttribute(attributes, "x-invalidValueReplacement", "invalidValueReplacement");
			} // End if

			if(compare(getSource(), Version.PMML_4_4) <= 0){

				if(hasAttribute(attributes, "invalidValueReplacement")){
					String invalidValueTreatment = getAttribute(attributes, "invalidValueTreatment");

					if(invalidValueTreatment != null){

						switch(invalidValueTreatment){
							case "asIs":
								{
									attributes = setAttribute(attributes, "invalidValueTreatment", "asValue");
								}
								break;
							case "asValue":
								break;
							default:
								break;
						}
					}
				}
			}
		} else

		if(("PMML").equals(localName)){
			Version target = getTarget();

			if(getExtensions()){
				attributes = renameAttribute(attributes, "version", "x-baseVersion");
			}

			attributes = setAttribute(attributes, "version", target.getVersion());
		} else

		if(("Segmentation").equals(localName)){

			if(compare(getSource(), Version.PMML_4_3) <= 0){
				String multipleModelMethod = getAttribute(attributes, "multipleModelMethod");

				attributes = renameAttribute(attributes, "x-missingPredictionTreatment", "missingPredictionTreatment");
				attributes = renameAttribute(attributes, "x-missingThreshold", "missingThreshold");

				if(multipleModelMethod != null){

					switch(multipleModelMethod){
						case "x-weightedMedian":
						case "x-weightedSum":
							{
								attributes = setAttribute(attributes, "multipleModelMethod", multipleModelMethod.substring("x-".length()));
							}
							break;
						default:
							break;
					}
				}
			}
		} else

		if(("TargetValue").equals(localName)){

			if(compare(getSource(), Version.PMML_3_1) <= 0){
				attributes = renameAttribute(attributes, "rawDataValue", "displayValue");
			}
		}

		return attributes;
	}

	public boolean getExtensions(){
		return this.extensions;
	}

	private void setExtensions(boolean extensions){
		this.extensions = extensions;
	}
}
