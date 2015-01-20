/*
 * Copyright (c) 2009 University of Tartu
 */
package org.jpmml.xjc;

import java.util.Collection;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JJavaName;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

public class PMMLPlugin extends Plugin {

	@Override
	public String getOptionName(){
		return "Xpmml";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		Collection<CClassInfo> classInfos = (model.beans()).values();
		for(CClassInfo classInfo : classInfos){

			Collection<CPropertyInfo> propertyInfos = classInfo.getProperties();
			for(CPropertyInfo propertyInfo : propertyInfos){
				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				// Collection of values
				if(propertyInfo.isCollection()){

					if((privateName).contains("And") || (privateName).contains("Or") || (privateName).equalsIgnoreCase("content")){
						propertyInfo.setName(true, "Content");
						propertyInfo.setName(false, "content");
					} else

					{
						// Have "arrays" instead of "arraies"
						if((privateName).endsWith("array") || (privateName).endsWith("Array")){
							publicName += "s";
							privateName += "s";
						} else

						// Have "refs" instead of "reves"
						if((privateName).endsWith("ref") || (privateName).endsWith("Ref")){
							publicName += "s";
							privateName += "s";
						} else

						{
							publicName = JJavaName.getPluralForm(publicName);
							privateName = JJavaName.getPluralForm(privateName);
						}

						propertyInfo.setName(true, publicName);
						propertyInfo.setName(false, privateName);
					}
				} else

				// Simple value
				{
					// This attribute is common to all Model subclasses. Here, programmatic customization is better than manual customization
					if((privateName).equals("isScorable")){
						propertyInfo.setName(true, "Scorable");
						propertyInfo.setName(false, "scorable");
					}
				}
			}
		}
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		Model model = outline.getModel();

		JCodeModel codeModel = model.codeModel;

		JClass hasIdInterface = codeModel.ref("org.dmg.pmml.HasId");
		JClass hasExtensionsInterface = codeModel.ref("org.dmg.pmml.HasExtensions");

		JClass iterableInterface = codeModel.ref("java.lang.Iterable");
		JClass iteratorInterface = codeModel.ref("java.util.Iterator");

		Collection<? extends ClassOutline> clazzes = outline.getClasses();
		for(ClassOutline clazz : clazzes){
			JDefinedClass definedClazz = clazz.implClass;

			FieldOutline idField = getIdField(clazz);
			if(idField != null){
				definedClazz._implements(hasIdInterface);
			}

			FieldOutline extensionsField = getExtensionsField(clazz);
			if(extensionsField != null){
				definedClazz._implements(hasExtensionsInterface);
			}

			FieldOutline contentField = getContentField(clazz);
			if(contentField != null){
				CPropertyInfo propertyInfo = contentField.getPropertyInfo();

				JFieldVar fieldVar = CodeModelUtil.getFieldVar(contentField);

				JType elementType = CodeModelUtil.getElementType(fieldVar.type());

				definedClazz._implements(iterableInterface.narrow(elementType));

				JMethod iteratorMethod = definedClazz.method(JMod.PUBLIC, iteratorInterface.narrow(elementType), "iterator");
				iteratorMethod.body()._return(JExpr.invoke("get" + propertyInfo.getName(true)).invoke("iterator"));
			}

			FieldOutline[] fields = clazz.getDeclaredFields();
			for(FieldOutline field : fields){
				CPropertyInfo propertyInfo = field.getPropertyInfo();

				if(propertyInfo.isCollection()){
					JFieldRef fieldRef = JExpr.refthis(propertyInfo.getName(false));

					JMethod hasElementsMethod = definedClazz.method(JMod.PUBLIC, boolean.class, "has" + propertyInfo.getName(true));
					hasElementsMethod.body()._return((fieldRef.ne(JExpr._null())).cand((fieldRef.invoke("size")).gt(JExpr.lit(0))));
				}
			}
		}

		return true;
	}

	static
	private FieldOutline getIdField(ClassOutline clazz){
		String name = "id";

		FieldOutline[] fields = clazz.getDeclaredFields();
		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();

			String privateName = propertyInfo.getName(false);

			JType fieldType = field.getRawType();

			if((name).equals(privateName) && ("java.lang.String").equals(fieldType.fullName())){
				return field;
			}
		}

		return null;
	}

	static
	private FieldOutline getExtensionsField(ClassOutline clazz){
		String name = "extensions";

		FieldOutline[] fields = clazz.getDeclaredFields();
		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();

			String privateName = propertyInfo.getName(false);

			JType fieldType = field.getRawType();

			if((name).equals(privateName) && propertyInfo.isCollection()){
				JType elementType = CodeModelUtil.getElementType(fieldType);

				if(("org.dmg.pmml.Extension").equals(elementType.fullName())){
					return field;
				}
			}
		}

		return null;
	}

	static
	private FieldOutline getContentField(ClassOutline clazz){
		String name = clazz.implClass.name();

		FieldOutline[] fields = clazz.getDeclaredFields();
		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();

			JType fieldType = field.getRawType();

			if(propertyInfo.isCollection()){
				JType elementType = CodeModelUtil.getElementType(fieldType);

				String elementName = elementType.name();

				if((name).equals(elementName + "s") || (name).equals(JJavaName.getPluralForm(elementName))){
					return field;
				}
			}
		}

		return null;
	}
}