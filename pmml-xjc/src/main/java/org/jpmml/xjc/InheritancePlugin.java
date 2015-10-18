/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CClassRef;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIClass;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.inheritance.Customizations;
import org.jvnet.jaxb2_commons.plugin.inheritance.ExtendsClass;
import org.jvnet.jaxb2_commons.plugin.inheritance.ImplementsInterface;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;
import org.xml.sax.ErrorHandler;

public class InheritancePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName(){
		return "Xinheritance";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public Collection<QName> getCustomizationElementNames(){
		return Arrays.asList(Customizations.EXTENDS_ELEMENT_NAME, Customizations.IMPLEMENTS_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		Map<String, CClassRef> classRefCache = new HashMap<>();

		CClassRef defaultBaseClazz = getClassRef(classRefCache, model, "org.dmg.pmml.PMMLObject");

		Collection<CClassInfo> classInfos = (model.beans()).values();
		for(CClassInfo classInfo : classInfos){
			CClassRef baseClazz = defaultBaseClazz;

			CPluginCustomization extendsCustomization = CustomizationUtils.findCustomization(classInfo, Customizations.EXTENDS_ELEMENT_NAME);
			if(extendsCustomization != null){
				ExtendsClass extendsClass = (ExtendsClass)CustomizationUtils.unmarshall(Customizations.getContext(), extendsCustomization);

				String name = getClassName(extendsClass);

				int lt = name.indexOf('<');
				if(lt > -1){
					name = name.substring(0, lt);
				}

				baseClazz = getClassRef(classRefCache, model, name);
			}

			classInfo.setBaseClass(baseClazz);
		}
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		Map<String, JClass> typeCache = new HashMap<>();

		Collection<? extends ClassOutline> clazzes = outline.getClasses();
		for(ClassOutline clazz : clazzes){
			JDefinedClass beanClazz = clazz.implClass;

			CPluginCustomization extendsCustomization = CustomizationUtils.findCustomization(clazz, Customizations.EXTENDS_ELEMENT_NAME);
			if(extendsCustomization != null){
				ExtendsClass extendsClass = (ExtendsClass)CustomizationUtils.unmarshall(Customizations.getContext(), extendsCustomization);

				JClass type = parseType(typeCache, codeModel, getClassName(extendsClass));

				beanClazz._extends(type);
			}

			List<CPluginCustomization> implementsCustomizations = CustomizationUtils.findCustomizations(clazz, Customizations.IMPLEMENTS_ELEMENT_NAME);
			for(CPluginCustomization implementsCustomization : implementsCustomizations){
				ImplementsInterface implementsInterface = (ImplementsInterface)CustomizationUtils.unmarshall(Customizations.getContext(), implementsCustomization);

				JClass type = parseType(typeCache, codeModel, getInterfaceName(implementsInterface));

				beanClazz._implements(type);
			}
		}

		return true;
	}

	static
	private String getClassName(ExtendsClass extendsClass){

		try {
			Field field = ExtendsClass.class.getDeclaredField("className");
			if(!field.isAccessible()){
				field.setAccessible(true);
			}

			return (String)field.get(extendsClass);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	static
	private String getInterfaceName(ImplementsInterface implementsInterface){
		return implementsInterface.getInterfaceName();
	}

	static
	private JClass parseType(Map<String, JClass> typeCache, JCodeModel codeModel, String name){
		JClass type = typeCache.get(name);

		if(type == null){

			try {
				type = (JClass)codeModel.parseType(name);
			} catch(ClassNotFoundException cnfe){
				throw new RuntimeException(cnfe);
			}

			typeCache.put(name, type);
		}

		return type;
	}

	static
	private CClassRef getClassRef(Map<String, CClassRef> classRefCache, Model model, String name){
		CClassRef classRef = classRefCache.get(name);

		if(classRef == null){
			classRef = new CClassRef(model, null, createBIClass(name), null);

			classRefCache.put(name, classRef);
		}

		return classRef;
	}

	static
	private BIClass createBIClass(String name){

		try {
			Constructor<? extends BIClass> constructor = BIClass.class.getDeclaredConstructor();
			if(!constructor.isAccessible()){
				constructor.setAccessible(true);
			}

			BIClass biClass = constructor.newInstance();

			Field field = BIClass.class.getDeclaredField("ref");
			if(!field.isAccessible()){
				field.setAccessible(true);
			}

			field.set(biClass, name);

			return biClass;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}