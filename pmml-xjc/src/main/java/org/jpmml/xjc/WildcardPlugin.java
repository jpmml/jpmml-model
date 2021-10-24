/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;
import org.glassfish.jaxb.core.v2.model.core.WildcardMode;
import org.xml.sax.ErrorHandler;

public class WildcardPlugin extends ComplexPlugin {

	@Override
	public String getOptionName(){
		return "Xwildcard";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public List<QName> getCustomizationElementNames(){
		return Collections.singletonList(WildcardPlugin.LAX_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		CPluginCustomization customization = CustomizationUtil.findCustomization(model, WildcardPlugin.LAX_ELEMENT_NAME);
		if(customization == null){
			return;
		}

		Collection<CClassInfo> classInfos = (model.beans()).values();
		for(CClassInfo classInfo : classInfos){
			Collection<CPropertyInfo> propertyInfos = classInfo.getProperties();

			for(CPropertyInfo propertyInfo : propertyInfos){

				if(propertyInfo instanceof CReferencePropertyInfo){
					CReferencePropertyInfo referencePropertyInfo = (CReferencePropertyInfo)propertyInfo;

					referencePropertyInfo.setWildcard(WildcardMode.LAX);
				}
			}
		}
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		return true;
	}

	public static final String NAMESPACE_URI = "http://jaxb2-commons.dev.java.net/basic/wildcard";

	public static final QName LAX_ELEMENT_NAME = new QName(WildcardPlugin.NAMESPACE_URI, "lax");
}