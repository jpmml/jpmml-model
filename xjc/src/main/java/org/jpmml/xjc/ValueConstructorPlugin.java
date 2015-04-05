/*
 * Copyright (c) 2010 University of Tartu
 */
package org.jpmml.xjc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.xml.sax.ErrorHandler;

public class ValueConstructorPlugin extends AbstractParameterizablePlugin {

	private boolean ignoreAttributes = false;

	private boolean ignoreElements = false;

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
		Collection<? extends ClassOutline> clazzes = outline.getClasses();

		for(ClassOutline clazz : clazzes){
			JDefinedClass beanClazz = clazz.implClass;

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			List<FieldOutline> superClassFields = getSuperClassFields(clazz);
			List<FieldOutline> classFields = getClassFields(clazz);

			if(superClassFields.size() > 0 || classFields.size() > 0){
				JMethod defaultConstructor = beanClazz.constructor(JMod.PUBLIC);
				JInvocation defaultSuperInvocation = defaultConstructor.body().invoke("super");

				JMethod valueConstructor = beanClazz.constructor(JMod.PUBLIC);
				JInvocation valueSuperInvocation = valueConstructor.body().invoke("super");

				for(FieldOutline superClassField : superClassFields){
					CPropertyInfo propertyInfo = superClassField.getPropertyInfo();

					JFieldVar superClassFieldVar = fieldVars.get(propertyInfo.getName(false));

					JVar param = valueConstructor.param(JMod.FINAL, superClassFieldVar.type(), superClassFieldVar.name());

					valueSuperInvocation.arg(param);
				}

				for(FieldOutline classField : classFields){
					CPropertyInfo propertyInfo = classField.getPropertyInfo();

					JFieldVar classFieldVar = fieldVars.get(propertyInfo.getName(false));

					JVar param = valueConstructor.param(JMod.FINAL, classFieldVar.type(), classFieldVar.name());

					valueConstructor.body().assign(JExpr.refthis(param.name()), param);
				}
			}
		}

		return true;
	}

	private List<FieldOutline> getSuperClassFields(ClassOutline clazz){
		List<FieldOutline> result = new ArrayList<FieldOutline>();

		for(ClassOutline superClazz = clazz.getSuperClass(); superClazz != null; superClazz = superClazz.getSuperClass()){
			result.addAll(0, getValueFields(superClazz));
		}

		return result;
	}

	private List<FieldOutline> getClassFields(ClassOutline clazz){
		return getValueFields(clazz);
	}

	private List<FieldOutline> getValueFields(ClassOutline clazz){
		List<FieldOutline> result = new ArrayList<FieldOutline>();

		JDefinedClass beanClazz = clazz.implClass;

		Map<String, JFieldVar> fieldVars = beanClazz.fields();

		FieldOutline[] fields = clazz.getDeclaredFields();
		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();

			JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

			JMods modifiers = fieldVar.mods();
			if((modifiers.getValue() & JMod.STATIC) == JMod.STATIC){
				continue;
			} // End if

			if(propertyInfo instanceof CAttributePropertyInfo && !getIgnoreAttributes()){
				CAttributePropertyInfo attributePropertyInfo = (CAttributePropertyInfo)propertyInfo;

				if(attributePropertyInfo.isRequired()){
					result.add(field);
				}
			} // End if

			if(propertyInfo instanceof CElementPropertyInfo && !getIgnoreElements()){
				CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

				if(elementPropertyInfo.isRequired()){
					result.add(field);
				}
			} // End if

			if(propertyInfo instanceof CValuePropertyInfo && !getIgnoreValues()){
				result.add(field);
			}
		}

		return result;
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

	public boolean getIgnoreValues(){
		return this.ignoreValues;
	}

	public void setIgnoreValues(boolean ignoreValues){
		this.ignoreValues = ignoreValues;
	}
}