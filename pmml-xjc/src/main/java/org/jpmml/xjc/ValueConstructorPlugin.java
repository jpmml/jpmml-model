/*
 * Copyright (c) 2010 University of Tartu
 */
package org.jpmml.xjc;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.xml.sax.ErrorHandler;

public class ValueConstructorPlugin extends AbstractParameterizablePlugin {

	private boolean ignoreAttributes = false;

	private boolean ignoreElements = false;

	private boolean ignoreReferences = false;

	private boolean ignoreValues = false;


	@Override
	public String getOptionName(){
		return "XvalueConstructor";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	@SuppressWarnings (
		value = {"unused"}
	)
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		JClass propertyAnnotation = codeModel.ref("org.jpmml.model.annotations.Property");

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			Predicate<FieldOutline> predicate = new Predicate<FieldOutline>(){

				@Override
				public boolean test(FieldOutline fieldOutline){
					CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

					JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

					JMods modifiers = fieldVar.mods();
					if((modifiers.getValue() & JMod.STATIC) == JMod.STATIC){
						return false;
					} // End if

					if(propertyInfo instanceof CAttributePropertyInfo){
						CAttributePropertyInfo attributePropertyInfo = (CAttributePropertyInfo)propertyInfo;

						return !getIgnoreAttributes() && attributePropertyInfo.isRequired();
					} else

					if(propertyInfo instanceof CElementPropertyInfo && !getIgnoreElements()){
						CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

						return !getIgnoreElements() && elementPropertyInfo.isRequired();
					} else

					if(propertyInfo instanceof CReferencePropertyInfo){
						CReferencePropertyInfo referencePropertyInfo = (CReferencePropertyInfo)propertyInfo;

						return !getIgnoreReferences() && referencePropertyInfo.isRequired();
					} else

					if(propertyInfo instanceof CValuePropertyInfo){
						CValuePropertyInfo valuePropertyInfo = (CValuePropertyInfo)propertyInfo;

						return !getIgnoreValues();
					} else

					{
						throw new IllegalArgumentException();
					}
				}
			};

			fieldOutlines = XJCUtil.filterFields(fieldOutlines, predicate);
			if(fieldOutlines.length == 0){
				continue;
			}

			JMethod defaultConstructor = beanClazz.constructor(JMod.PUBLIC);
			JMethod valueConstructor = beanClazz.constructor(JMod.PUBLIC);

			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				JVar param = valueConstructor.param(fieldVar.type(), fieldVar.name());

				param.annotate(propertyAnnotation).param("value", fieldVar.name());

				valueConstructor.body().assign(JExpr.refthis(fieldVar.name()), param);
			}
		}

		return true;
	}

	public boolean getIgnoreAttributes(){
		return this.ignoreAttributes;
	}

	public void setIgnoreAttributes(boolean ignoreAttributes){
		this.ignoreAttributes = ignoreAttributes;
	}

	public boolean getIgnoreElements(){
		return this.ignoreElements;
	}

	public void setIgnoreElements(boolean ignoreElements){
		this.ignoreElements = ignoreElements;
	}

	public boolean getIgnoreReferences(){
		return this.ignoreReferences;
	}

	public void setIgnoreReferences(boolean ignoreReferences){
		this.ignoreReferences = ignoreReferences;
	}

	public boolean getIgnoreValues(){
		return this.ignoreValues;
	}

	public void setIgnoreValues(boolean ignoreValues){
		this.ignoreValues = ignoreValues;
	}
}