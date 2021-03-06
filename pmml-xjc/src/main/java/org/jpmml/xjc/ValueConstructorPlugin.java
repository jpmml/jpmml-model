/*
 * Copyright (c) 2010 University of Tartu
 */
package org.jpmml.xjc;

import java.math.BigInteger;
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
import com.sun.codemodel.JType;
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
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;
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
		JClass valueConstructorAnnotation = codeModel.ref("org.jpmml.model.annotations.ValueConstructor");

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			Predicate<FieldOutline> predicate = new Predicate<FieldOutline>(){

				@Override
				public boolean test(FieldOutline fieldOutline){
					CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

					XSComponent xsComponent = propertyInfo.getSchemaComponent();

					JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

					JMods modifiers = fieldVar.mods();
					if((modifiers.getValue() & JMod.STATIC) == JMod.STATIC){
						return false;
					} // End if

					if(propertyInfo instanceof CAttributePropertyInfo){
						CAttributePropertyInfo attributePropertyInfo = (CAttributePropertyInfo)propertyInfo;

						boolean required = attributePropertyInfo.isRequired();

						switch(beanClazz.fullName()){
							case "org.dmg.pmml.DataField":
							case "org.dmg.pmml.DefineFunction":
							case "org.dmg.pmml.DerivedField":
							case "org.dmg.pmml.OutputField":
								{
									switch(fieldVar.name()){
										case "name":
										case "opType":
										case "dataType":
											required |= true;
											break;
										default:
											break;
									}
								}
								break;
							case "org.dmg.pmml.SimplePredicate":
								{
									switch(fieldVar.name()){
										case "value":
											required |= true;
											break;
										default:
											break;
									}
								}
								break;
							default:
								break;
						}

						return !getIgnoreAttributes() && required;
					} else

					if(propertyInfo instanceof CElementPropertyInfo && !getIgnoreElements()){
						CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

						switch((fieldVar.type()).fullName()){
							case "org.dmg.pmml.EmbeddedModel":
								return false;
							default:
								break;
						}

						boolean required = elementPropertyInfo.isRequired();

						if(xsComponent instanceof XSParticle){
							XSParticle xsParticle = (XSParticle)xsComponent;

							BigInteger minOccurs = xsParticle.getMinOccurs();
							BigInteger maxOccurs = xsParticle.getMaxOccurs();

							required |= (minOccurs.intValue() >= 1);
						}

						switch(beanClazz.fullName()){
							case "org.dmg.pmml.OutputField":
								{
									switch(fieldVar.name()){
										case "expression":
											required = false;
											break;
										default:
											break;
									}
								}
								break;
							default:
								break;
						}

						return !getIgnoreElements() && required;
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

			JMethod defaultConstructor = beanClazz.getConstructor(new JType[0]);
			if(defaultConstructor == null){
				defaultConstructor = beanClazz.constructor(JMod.PUBLIC);
			}

			JMethod valueConstructor = beanClazz.constructor(JMod.PUBLIC);
			valueConstructor.annotate(valueConstructorAnnotation);

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