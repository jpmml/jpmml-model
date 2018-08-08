/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.xml.sax.ErrorHandler;

public class JacksonPlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName(){
		return "Xjackson";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			JAnnotationUse jsonAutoDetect = beanClazz.annotate(JsonAutoDetect.class)
				.param("fieldVisibility", JsonAutoDetect.Visibility.ANY)
				.param("getterVisibility", JsonAutoDetect.Visibility.NONE)
				.param("isGetterVisibility", JsonAutoDetect.Visibility.NONE)
				.param("setterVisibility", JsonAutoDetect.Visibility.NONE);

			JAnnotationUse jsonInclude = beanClazz.annotate(JsonInclude.class)
				.param("value", JsonInclude.Include.NON_EMPTY);

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			if(fieldOutlines.length == 0){
				continue;
			}

			JAnnotationUse jsonPropertyOrder = beanClazz.annotate(JsonPropertyOrder.class);

			JAnnotationArrayMember fieldNameArray = jsonPropertyOrder.paramArray("value");

			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				fieldNameArray.param(propertyInfo.getName(false));
			}
		}

		return true;
	}
}