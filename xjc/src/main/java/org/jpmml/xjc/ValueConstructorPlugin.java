/*
 * Copyright (c) 2010 University of Tartu
 */
package org.jpmml.xjc;

import java.util.*;

import com.sun.codemodel.*;
import com.sun.tools.xjc.*;
import com.sun.tools.xjc.model.*;
import com.sun.tools.xjc.outline.*;

import org.jvnet.jaxb2_commons.plugin.*;

import org.xml.sax.*;

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
			List<FieldOutline> superClassFields = getSuperClassFields(clazz);
			List<FieldOutline> classFields = getClassFields(clazz);

			if(superClassFields.size() > 0 || classFields.size() > 0){
				JMethod defaultConstructor = (clazz.implClass).constructor(JMod.PUBLIC);
				defaultConstructor.annotate(Deprecated.class);
				JInvocation defaultSuperInvocation = defaultConstructor.body().invoke("super");

				JMethod valueConstructor = (clazz.implClass).constructor(JMod.PUBLIC);
				JInvocation valueSuperInvocation = valueConstructor.body().invoke("super");

				for(FieldOutline superClassField : superClassFields){
					JFieldVar superClassFieldVar = CodeModelUtil.getFieldVar(superClassField);

					JVar param = valueConstructor.param(JMod.FINAL, superClassFieldVar.type(), superClassFieldVar.name());

					valueSuperInvocation.arg(param);
				}

				for(FieldOutline classField : classFields){
					JFieldVar classFieldVar = CodeModelUtil.getFieldVar(classField);

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

		FieldOutline[] fields = clazz.getDeclaredFields();
		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();

			if(propertyInfo.isCollection()){
				continue;
			}

			JFieldVar fieldVar = CodeModelUtil.getFieldVar(field);

			int modifiers = (fieldVar.mods()).getValue();
			if((modifiers & JMod.STATIC) == JMod.STATIC){
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