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
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

public class AnnotatePlugin extends ComplexPlugin {

	@Override
	public String getOptionName(){
		return "Xannotate";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public List<QName> getCustomizationElementNames(){
		return Arrays.asList(AnnotatePlugin.ANNOTATE_CLASS_QNAME, AnnotatePlugin.ANNOTATE_ENUM_QNAME, AnnotatePlugin.ANNOTATE_ENUM_CONSTANT_QNAME, AnnotatePlugin.ANNOTATE_PROPERTY_QNAME);
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		JClass collectionElementTypeAnnotation = codeModel.ref("org.jpmml.model.annotations.CollectionElementType");

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			CPluginCustomization classCustomization = CustomizationUtil.findCustomization(classOutline, AnnotatePlugin.ANNOTATE_CLASS_QNAME);
			if(classCustomization != null){
				annotate(codeModel, beanClazz, classCustomization);
			}

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				if(propertyInfo instanceof CElementPropertyInfo){
					CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

					JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

					if(elementPropertyInfo.isCollection()){
						JClass elementType = (JClass)CodeModelUtil.getElementType(fieldVar.type());

						JAnnotationUse collectionElementType = fieldVar.annotate(collectionElementTypeAnnotation)
							.param("value", elementType.dotclass());
					}
				}

				List<CPluginCustomization> propertyCustomizations = CustomizationUtil.findPropertyCustomizationsInProperty(propertyInfo, AnnotatePlugin.ANNOTATE_PROPERTY_QNAME);
				for(CPluginCustomization propertyCustomization : propertyCustomizations){
					JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

					annotate(codeModel, fieldVar, propertyCustomization);
				}
			}
		}

		Collection<? extends EnumOutline> enumOutlines = outline.getEnums();
		for(EnumOutline enumOutline : enumOutlines){
			JDefinedClass clazz = enumOutline.clazz;

			CPluginCustomization enumCustomization = CustomizationUtil.findCustomization(enumOutline, AnnotatePlugin.ANNOTATE_ENUM_QNAME);
			if(enumCustomization != null){
				annotate(codeModel, clazz, enumCustomization);
			}

			List<EnumConstantOutline> enumConstantOutlines = enumOutline.constants;
			for(EnumConstantOutline enumConstantOutline : enumConstantOutlines){
				List<CPluginCustomization> enumConstantCustomizations = CustomizationUtil.findCustomizations(enumConstantOutline.target, AnnotatePlugin.ANNOTATE_ENUM_CONSTANT_QNAME);

				for(CPluginCustomization enumConstantCustomization : enumConstantCustomizations){
					annotate(codeModel, enumConstantOutline.constRef, enumConstantCustomization);
				}
			}
		}

		return true;
	}

	static
	void annotate(JCodeModel codeModel, JAnnotatable owner, CPluginCustomization customization){
		String[] classAndValue = parseCustomization(customization);

		switch(classAndValue[0]){
			case "org.jpmml.model.annotations.NullSafeGetter":
			case "org.jpmml.model.annotations.ValueConstructorParameter":
				return;
			default:
				break;
		}

		JClass annotationClass = codeModel.ref(classAndValue[0]);

		JAnnotationUse annotationUse = owner.annotate(annotationClass);

		if(classAndValue.length > 1){
			String[] params = classAndValue[1].split(",");

			for(String param : params){
				String name;
				String value;

				int index = param.indexOf('=');
				if(index < 0){
					name = "value";
					value = param;
				} else

				{
					name = (param.substring(0, index)).trim();
					value = (param.substring(index + 1)).trim();
				}

				annotationUse = annotationUse.param(name, JExpr.direct(value));
			}
		}
	}

	static
	String[] parseCustomization(CPluginCustomization customization){
 		String content = (customization.element).getTextContent();

 		Matcher matcher = AnnotatePlugin.PATTERN.matcher(content);
 		if(matcher.matches()){
			return new String[]{matcher.group(1), matcher.group(2)};
		} else

		{
			return new String[]{content};
 		}
 	}

	private static final Pattern PATTERN = Pattern.compile("(.+)\\((.+)\\)");

	public static final String NAMESPACE_URI = "http://annox.dev.java.net";

	public static final QName ANNOTATE_CLASS_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateClass");
	public static final QName ANNOTATE_ENUM_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateEnum");
	public static final QName ANNOTATE_ENUM_CONSTANT_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateEnumConstant");
	public static final QName ANNOTATE_PROPERTY_QNAME = new QName(AnnotatePlugin.NAMESPACE_URI, "annotateProperty");
}