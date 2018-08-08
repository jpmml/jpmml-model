/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CCustomizations;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

public class AnnotatePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName(){
		return "Xannotate";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public Collection<QName> getCustomizationElementNames(){
		return Arrays.asList(AnnotatePlugin.ANNOTATE_CLASS_QNAME, AnnotatePlugin.ANNOTATE_ENUM_QNAME, AnnotatePlugin.ANNOTATE_ENUM_CONSTANT_QNAME, AnnotatePlugin.ANNOTATE_PROPERTY_QNAME);
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			CPluginCustomization classCustomization = CustomizationUtils.findCustomization(classOutline, AnnotatePlugin.ANNOTATE_CLASS_QNAME);
			if(classCustomization != null){
				annotate(codeModel, beanClazz, classCustomization);
			}

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				List<CPluginCustomization> propertyCustomizations = CustomizationUtils.findPropertyCustomizationsInProperty(propertyInfo, AnnotatePlugin.ANNOTATE_PROPERTY_QNAME);
				for(CPluginCustomization propertyCustomization : propertyCustomizations){
					JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

					annotate(codeModel, fieldVar, propertyCustomization);
				}
			}
		}

		Collection<? extends EnumOutline> enumOutlines = outline.getEnums();
		for(EnumOutline enumOutline : enumOutlines){
			JDefinedClass clazz = enumOutline.clazz;

			CPluginCustomization enumCustomization = CustomizationUtils.findCustomization(enumOutline, AnnotatePlugin.ANNOTATE_ENUM_QNAME);
			if(enumCustomization != null){
				annotate(codeModel, clazz, enumCustomization);
			}

			List<EnumConstantOutline> enumConstantOutlines = enumOutline.constants;
			for(EnumConstantOutline enumConstantOutline : enumConstantOutlines){
				CCustomizations enumConstantCustomizations = enumConstantOutline.target.getCustomizations();

				for(CPluginCustomization enumConstantCustomization : enumConstantCustomizations){
					Element element = enumConstantCustomization.element;

					if((AnnotatePlugin.ANNOTATE_ENUM_CONSTANT_QNAME.getNamespaceURI()).equals(element.getNamespaceURI()) && (AnnotatePlugin.ANNOTATE_ENUM_CONSTANT_QNAME.getLocalPart()).equals(element.getLocalName())){
						annotate(codeModel, enumConstantOutline.constRef, enumConstantCustomization);

						enumConstantCustomization.markAsAcknowledged();
					}
				}
			}
		}

		return true;
	}

	static
	private JAnnotationUse annotate(JCodeModel codeModel, JAnnotatable owner, CPluginCustomization customization){
		String content = (customization.element).getTextContent();

		Matcher matcher = AnnotatePlugin.PATTERN.matcher(content);
		if(matcher.matches()){
			JClass annotationClass = codeModel.ref(matcher.group(1));

			return owner.annotate(annotationClass)
				.param("value", JExpr.direct(matcher.group(2)));
		} else

		{
			JClass annotationClass = codeModel.ref(content);

			return owner.annotate(annotationClass);
		}
	}

	private static final Pattern PATTERN = Pattern.compile("(.+)\\((.+)\\)");

	private static final String NAMESPACE_URI = "http://annox.dev.java.net";

	private static final QName ANNOTATE_CLASS_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateClass");
	private static final QName ANNOTATE_ENUM_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateEnum");
	private static final QName ANNOTATE_ENUM_CONSTANT_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateEnumConstant");
	private static final QName ANNOTATE_PROPERTY_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateProperty");
}