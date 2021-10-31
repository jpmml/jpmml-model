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
import javax.xml.transform.dom.DOMSource;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CClassRef;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIClass;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import org.xml.sax.ErrorHandler;

public class InheritancePlugin extends ComplexPlugin {

	@Override
	public String getOptionName(){
		return "Xinheritance";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public List<QName> getCustomizationElementNames(){
		return Arrays.asList(InheritancePlugin.EXTENDS_ELEMENT_NAME, InheritancePlugin.IMPLEMENTS_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		Map<String, CClassRef> classRefCache = new HashMap<>();

		CClassRef defaultBaseClazz = getClassRef(classRefCache, model, "org.dmg.pmml.PMMLObject");

		Collection<CClassInfo> classInfos = (model.beans()).values();
		for(CClassInfo classInfo : classInfos){
			CClassRef baseClazz = defaultBaseClazz;

			CPluginCustomization extendsCustomization = CustomizationUtil.findCustomization(classInfo, InheritancePlugin.EXTENDS_ELEMENT_NAME);
			if(extendsCustomization != null){
				ExtendsClass extendsClass = (ExtendsClass)unmarshal(extendsCustomization);

				String name = extendsClass.className;

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

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			CPluginCustomization extendsCustomization = CustomizationUtil.findCustomization(classOutline, InheritancePlugin.EXTENDS_ELEMENT_NAME);
			if(extendsCustomization != null){
				ExtendsClass extendsClass = (ExtendsClass)unmarshal(extendsCustomization);

				JClass type = parseType(typeCache, codeModel, extendsClass.className);

				beanClazz._extends(type);
			}

			List<CPluginCustomization> implementsCustomizations = CustomizationUtil.findCustomizations(classOutline, InheritancePlugin.IMPLEMENTS_ELEMENT_NAME);
			for(CPluginCustomization implementsCustomization : implementsCustomizations){
				ImplementsInterface implementsInterface = (ImplementsInterface)unmarshal(implementsCustomization);

				JClass type = parseType(typeCache, codeModel, implementsInterface.interfaceName);

				beanClazz._implements(type);
			}

			// See https://github.com/highsource/jaxb2-basics/issues/70
			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				CustomizationUtil.findPropertyCustomizationsInProperty(propertyInfo, InheritancePlugin.EXTENDS_ELEMENT_NAME);
				CustomizationUtil.findPropertyCustomizationsInProperty(propertyInfo, InheritancePlugin.IMPLEMENTS_ELEMENT_NAME);
			}
		}

		return true;
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
			Constructor<? extends BIClass> biClassConstructor = BIClass.class.getDeclaredConstructor();
			if(!biClassConstructor.isAccessible()){
				biClassConstructor.setAccessible(true);
			}

			BIClass biClass = biClassConstructor.newInstance();

			Field refField = BIClass.class.getDeclaredField("ref");
			if(!refField.isAccessible()){
				refField.setAccessible(true);
			}

			refField.set(biClass, name);

			return biClass;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	static
	private Object unmarshal(CPluginCustomization customization){

		try {
			Unmarshaller unmarshaller = InheritancePlugin.context.createUnmarshaller();

			return unmarshaller.unmarshal(new DOMSource(customization.element));
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public static final String NAMESPACE_URI = "http://jaxb2-commons.dev.java.net/basic/inheritance";

	public static final QName EXTENDS_ELEMENT_NAME = new QName(InheritancePlugin.NAMESPACE_URI, "extends");
	public static final QName IMPLEMENTS_ELEMENT_NAME = new QName(InheritancePlugin.NAMESPACE_URI, "implements");

	@XmlRootElement (
		namespace = NAMESPACE_URI,
		name = "extends"
	)
	static
	public class ExtendsClass {

		@XmlValue
		public String className;
	}

	@XmlRootElement (
		namespace = NAMESPACE_URI,
		name = "implements"
	)
	static
	public class ImplementsInterface {

		@XmlValue
		public String interfaceName;
	}

	private static final JAXBContext context;

	static {

		try {
			context = JAXBContext.newInstance(ExtendsClass.class, ImplementsInterface.class);
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}