/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.bind.api.impl.NameConverter;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

public class UnsupportedPropertyPlugin extends Plugin {

	@Override
	public String getOptionName(){
		return "XunsupportedProperty";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public List<String> getCustomizationURIs(){
		return Collections.singletonList(JAVA_URI);
	}

	@Override
	public boolean isCustomizationTagName(String nsUri, String localName){
		return nsUri.equals(JAVA_URI) && localName.equals("unsupportedProperty");
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		Model model = outline.getModel();

		JCodeModel codeModel = model.codeModel;

		// XXX
		JClass exceptionClazz = codeModel.ref(UnsupportedOperationException.class);

		Collection<? extends ClassOutline> clazzes = outline.getClasses();
		for(ClassOutline clazz : clazzes){
			JDefinedClass beanClazz = clazz.implClass;

			List<CPluginCustomization> customizations = PluginUtil.getAllCustomizations(clazz.target, this);
			for(CPluginCustomization customization : customizations){
				Element element = customization.element;

				String property = element.getAttribute("property");
				if(property == null){
					throw new IllegalArgumentException();
				}

				String propertyName = NameConverter.standard.toPropertyName(property);

				String name = element.getAttribute("name");
				if(name == null){
					throw new IllegalArgumentException();
				}

				JClass nameClazz = codeModel.ref(name);

				JMethod getterMethod = beanClazz.method(JMod.PUBLIC, nameClazz, "get" + propertyName);
				getterMethod.javadoc().append("Gets the value of the " + property + " property.").addThrows(exceptionClazz).append("Always.");
				getterMethod.annotate(Override.class);
				getterMethod.body()._throw(JExpr._new(exceptionClazz));

				JMethod setterMethod = beanClazz.method(JMod.PUBLIC, beanClazz, "set" + propertyName);
				setterMethod.javadoc().append("Sets the value of the " + property + " property.").addThrows(exceptionClazz).append("Always.");
				setterMethod.annotate(Override.class);
				setterMethod.param(nameClazz, property);
				setterMethod.body()._throw(JExpr._new(exceptionClazz));

				customization.markAsAcknowledged();
			}
		}

		return true;
	}

	private static final String JAVA_URI = "http://java.sun.com/java";
}