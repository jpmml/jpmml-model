/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.filters;

import java.util.HashMap;
import java.util.Map;

import org.dmg.pmml.Version;
import org.xml.sax.XMLReader;

/**
 * <p>
 * A SAX filter that replaced HTTPS protocol XML namespace URIs with HTTP ones.
 * </p>
 *
 * @see <a href="http://mantis.dmg.org/view.php?id=274">Invalid PMML XML namespace URI published on website</a>
 */
public class Issue274Filter extends NamespaceFilter {

	public Issue274Filter(){
	}

	public Issue274Filter(XMLReader reader){
		super(reader);
	}

	@Override
	public String filterNamespaceURI(String namespaceURI){

		if(namespaceURI.startsWith("https://")){
			String standardizedNamespaceURI = Issue274Filter.mapping.get(namespaceURI);

			if(standardizedNamespaceURI != null){
				return standardizedNamespaceURI;
			}
		}

		return namespaceURI;
	}

	private static final Map<String, String> mapping = new HashMap<>();

	static {
		Version[] versions = Version.values();

		for(Version version : versions){
			String namespaceURI = version.getNamespaceURI();

			if(!version.isStandard()){
				continue;
			}

			mapping.put(namespaceURI.replace("http://", "https://"), namespaceURI);
		}
	}
}