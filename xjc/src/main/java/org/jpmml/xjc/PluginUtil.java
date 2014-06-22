/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CCustomizable;
import com.sun.tools.xjc.model.CPluginCustomization;
import org.w3c.dom.Element;

public class PluginUtil {

	private PluginUtil(){
	}

	static
	public CPluginCustomization getCustomization(CCustomizable customizable, Plugin plugin){
		List<CPluginCustomization> customizations = getAllCustomizations(customizable, plugin);

		if(customizations.size() == 0){
			return null;
		} else

		if(customizations.size() == 1){
			return customizations.get(0);
		}

		throw new IllegalStateException();
	}

	static
	public List<CPluginCustomization> getAllCustomizations(CCustomizable customizable, Plugin plugin){
		List<CPluginCustomization> result = new ArrayList<CPluginCustomization>();

		Iterator<CPluginCustomization> it = (customizable.getCustomizations()).iterator();
		while(it.hasNext()){
			CPluginCustomization customization = it.next();

			Element element = customization.element;

			if(plugin.isCustomizationTagName(element.getNamespaceURI(), element.getLocalName())){
				result.add(customization);
			}
		}

		return result;
	}
}