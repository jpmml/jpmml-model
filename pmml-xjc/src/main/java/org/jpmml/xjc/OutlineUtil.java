/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.math.BigInteger;
import java.util.Map;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JMods;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;

public class OutlineUtil {

	private OutlineUtil(){
	}

	static
	public boolean isRequired(JDefinedClass beanClazz, CPropertyInfo propertyInfo, boolean constructor){
		Map<String, JFieldVar> fieldVars = beanClazz.fields();

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
								required |= true;
								break;
							case "opType":
							case "dataType":
								required |= constructor;
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
								required |= constructor;
								break;
							default:
								break;
						}
					}
					break;
				default:
					break;
			}

			return required;
		} else

		if(propertyInfo instanceof CElementPropertyInfo){
			CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

			switch((fieldVar.type()).fullName()){
				case "org.dmg.pmml.EmbeddedModel":
				//case "org.dmg.pmml.InlineTable":
				case "org.dmg.pmml.TableLocator":
					return false;
				default:
					break;
			}

			boolean required = elementPropertyInfo.isRequired();

			XSComponent xsComponent = propertyInfo.getSchemaComponent();
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

			return required;
		} else

		if(propertyInfo instanceof CReferencePropertyInfo){
			CReferencePropertyInfo referencePropertyInfo = (CReferencePropertyInfo)propertyInfo;

			return referencePropertyInfo.isRequired();
		} else

		if(propertyInfo instanceof CValuePropertyInfo){
			CValuePropertyInfo valuePropertyInfo = (CValuePropertyInfo)propertyInfo;

			return true;
		} else

		{
			throw new IllegalArgumentException();
		}

	}
}