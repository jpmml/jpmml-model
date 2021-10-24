/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import com.sun.tools.xjc.Plugin;

abstract
public class ComplexPlugin extends Plugin {

	abstract
	public List<QName> getCustomizationElementNames();

	@Override
	public List<String> getCustomizationURIs(){
		List<QName> customizationElementNames = getCustomizationElementNames();

		return customizationElementNames.stream()
			.map(xmlName -> xmlName.getNamespaceURI())
			.collect(Collectors.toList());
	}

	@Override
	public boolean isCustomizationTagName(String namespaceURI, String localPart){
		List<QName> customizationElementNames = getCustomizationElementNames();

		Optional<QName> match = customizationElementNames.stream()
			.filter(xmlName -> Objects.equals(xmlName.getNamespaceURI(), namespaceURI) && Objects.equals(xmlName.getLocalPart(), localPart))
			.findAny();

		return match.isPresent();
	}
}