/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.istack.build.NameConverter;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.CTypeRef;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
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
			CClassInfo classInfo = classOutline.target;
			JDefinedClass beanClazz = classOutline.implClass;

			if(classInfo.isElement()){
				QName elementName = classInfo.getElementName();

				JAnnotationUse jsonRootName = beanClazz.annotate(JsonRootName.class)
					.param("value", elementName.getLocalPart());
			}

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			if(fieldOutlines.length == 0){
				continue;
			}

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			JAnnotationUse jsonPropertyOrder = beanClazz.annotate(JsonPropertyOrder.class);

			JAnnotationArrayMember fieldNameArray = jsonPropertyOrder.paramArray("value");

			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				String propertyName = fieldVar.name();

				fieldNameArray.param(propertyName);

				if(propertyInfo instanceof CAttributePropertyInfo){
					CAttributePropertyInfo attributePropertyInfo = (CAttributePropertyInfo)propertyInfo;

					QName xmlName = attributePropertyInfo.getXmlName();

					JAnnotationUse jsonProperty = fieldVar.annotate(JsonProperty.class)
						.param("value", xmlName.getLocalPart());
				} else

				if(propertyInfo instanceof CElementPropertyInfo){
					CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

					List<CTypeRef> types = elementPropertyInfo.getTypes();

					// XXX
					if(("node").equals(propertyName) || ("nodes").equals(propertyName)){
						JAnnotationUse jsonProperty = fieldVar.annotate(JsonProperty.class)
							.param("value", "Node");

						JAnnotationUse jsonTypeInfo = fieldVar.annotate(JsonTypeInfo.class)
							.param("include", JsonTypeInfo.As.WRAPPER_OBJECT)
							.param("use", JsonTypeInfo.Id.NAME);

						JAnnotationUse jsonSubTypes = fieldVar.annotate(JsonSubTypes.class);

						JAnnotationArrayMember valueArray = jsonSubTypes.paramArray("value");

						JClass complexNodeClass = codeModel.ref("org.dmg.pmml.tree.ComplexNode");

						JAnnotationUse jsonSubTypesType = jsonSubTypes.annotate(JsonSubTypes.Type.class)
							.param("name", "Node")
							.param("value", JExpr.dotclass(complexNodeClass));

						valueArray.param(jsonSubTypesType);
					} else

					if(types.size() == 1){
						CTypeRef type = types.get(0);

						QName xmlName = type.getTagName();

						JAnnotationUse jsonProperty = fieldVar.annotate(JsonProperty.class)
							.param("value", xmlName.getLocalPart());
					} else

					if(types.size() > 1){

						if(!(propertyName).equals("content")){
							propertyName = NameConverter.standard.toPropertyName(propertyName);

							if(propertyName.endsWith("s")){
								propertyName = propertyName.substring(0, propertyName.length() - 1);
							}
						}

						JAnnotationUse jsonProperty = fieldVar.annotate(JsonProperty.class)
							.param("value", propertyName);

						JAnnotationUse jsonTypeInfo = fieldVar.annotate(JsonTypeInfo.class)
							.param("include", JsonTypeInfo.As.WRAPPER_OBJECT)
							.param("use", JsonTypeInfo.Id.NAME);

						JAnnotationUse jsonSubTypes = fieldVar.annotate(JsonSubTypes.class);

						JAnnotationArrayMember valueArray = jsonSubTypes.paramArray("value");

						for(CTypeRef type : types){
							CClassInfo typeClassInfo = (CClassInfo)type.getTarget();

							JClass clazz = codeModel.ref(typeClassInfo.fullName());

							JAnnotationUse jsonSubTypesType = jsonSubTypes.annotate(JsonSubTypes.Type.class)
								.param("name", clazz.name())
								.param("value", JExpr.dotclass(clazz));

							valueArray.param(jsonSubTypesType);
						}
					} else

					{
						throw new RuntimeException();
					}
				} else

				if(propertyInfo instanceof CReferencePropertyInfo){
					CReferencePropertyInfo referencePropertyInfo = (CReferencePropertyInfo)propertyInfo;

					JAnnotationUse jsonProperty = fieldVar.annotate(JsonProperty.class)
						.param("value", "content");
				} else

				if(propertyInfo instanceof CValuePropertyInfo){
					CValuePropertyInfo valuePropertyInfo = (CValuePropertyInfo)propertyInfo;

					JAnnotationUse jsonProperty = fieldVar.annotate(JsonProperty.class)
						.param("value", "value");
				} else

				{
					throw new RuntimeException();
				}
			}
		}

		Collection<? extends EnumOutline> enumOutlines = outline.getEnums();
		for(EnumOutline enumOutline : enumOutlines){
			JDefinedClass clazz = enumOutline.clazz;

			List<EnumConstantOutline> enumConstantOutlines = enumOutline.constants;
			for(EnumConstantOutline enumConstantOutline : enumConstantOutlines){
				JEnumConstant enumConstant = enumConstantOutline.constRef;

				enumConstant.annotate(JsonProperty.class)
					.param("value", enumConstantOutline.target.getLexicalValue());
			}
		}

		return true;
	}
}