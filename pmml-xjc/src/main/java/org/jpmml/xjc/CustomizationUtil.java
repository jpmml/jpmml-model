/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import com.sun.tools.xjc.model.CCustomizable;
import com.sun.tools.xjc.model.CCustomizations;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.CustomizableOutline;
import org.w3c.dom.Element;

public class CustomizationUtil {

	private CustomizationUtil(){
	}

	static
	public CPluginCustomization findCustomization(CustomizableOutline customizableOutline, QName xmlName){
		return findCustomization(customizableOutline.getTarget(), xmlName);
	}

	static
	public CPluginCustomization findCustomization(CCustomizable customizable, QName xmlName){
		CCustomizations customizations = customizable.getCustomizations();

		return customizations.stream()
			.filter(customization -> match(customization, xmlName))
			.findFirst().orElse(null);
	}

	static
	public List<CPluginCustomization> findCustomizations(CustomizableOutline customizableOutline, QName xmlName){
		return findCustomizations(customizableOutline.getTarget(), xmlName);
	}

	static
	public List<CPluginCustomization> findCustomizations(CCustomizable customizable, QName xmlName){
		CCustomizations customizations = customizable.getCustomizations();

		return customizations.stream()
			.filter(customization -> match(customization, xmlName))
			.collect(Collectors.toList());
	}

	static
	public List<CPluginCustomization> findPropertyCustomizationsInProperty(CPropertyInfo propertyInfo, QName xmlName){
		// XXX
		return findCustomizations(propertyInfo, xmlName);
	}

	static
	public boolean match(CPluginCustomization customization, QName xmlName){
		Element element = customization.element;

		boolean equals = Objects.equals(fixNull(element.getNamespaceURI()), xmlName.getNamespaceURI()) && Objects.equals(fixNull(element.getLocalName()), xmlName.getLocalPart());
		if(equals){
			customization.markAsAcknowledged();
		}

		return equals;
	}

	static
	private String fixNull(String string){

		if(string == null){
			return "";
		}

		return string;
	}
}