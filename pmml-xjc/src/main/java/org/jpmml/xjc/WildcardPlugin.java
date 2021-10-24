/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.Model;
import org.glassfish.jaxb.core.v2.model.core.WildcardMode;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.wildcard.Customizations;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;
import org.xml.sax.ErrorHandler;

public class WildcardPlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName(){
		return "Xwildcard";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public Collection<QName> getCustomizationElementNames(){
		return Arrays.asList(Customizations.LAX_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		CPluginCustomization laxModel = CustomizationUtils.findCustomization(model, Customizations.LAX_ELEMENT_NAME);
		if(laxModel == null){
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
}