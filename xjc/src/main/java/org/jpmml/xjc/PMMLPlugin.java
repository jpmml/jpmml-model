/*
 * Copyright (c) 2009 University of Tartu
 */
package org.jpmml.xjc;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JJavaName;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
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

		Comparator<CPropertyInfo> comparator = new Comparator<CPropertyInfo>(){

			@Override
			public int compare(CPropertyInfo left, CPropertyInfo right){
				boolean leftAttribute = (left instanceof CAttributePropertyInfo);
				boolean rightAttribute = (right instanceof CAttributePropertyInfo);

				if(leftAttribute && !rightAttribute){
					return -1;
				} else

				if(!leftAttribute && rightAttribute){
					return 1;
				}

				return 0;
			}
		};

		Collection<CClassInfo> classInfos = (model.beans()).values();
		for(CClassInfo classInfo : classInfos){
			List<CPropertyInfo> propertyInfos = classInfo.getProperties();
			Collections.sort(propertyInfos, comparator);

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
			JDefinedClass beanClazz = clazz.implClass;

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			String fullName = beanClazz.fullName();

			if(("org.dmg.pmml.IntSparseArray").equals(fullName)){
				JClass superClazz = beanClazz._extends();

				beanClazz._extends(superClazz.narrow(Integer.class));
			} else

			if(("org.dmg.pmml.RealSparseArray").equals(fullName)){
				JClass superClazz = beanClazz._extends();

				beanClazz._extends(superClazz.narrow(Double.class));
			}

			FieldOutline idField = getIdField(clazz);
			if(idField != null){
				beanClazz._implements(hasIdInterface);
			}

			FieldOutline extensionsField = getExtensionsField(clazz);
			if(extensionsField != null){
				beanClazz._implements(hasExtensionsInterface);
			}

			FieldOutline contentField = getContentField(clazz);
			if(contentField != null){
				CPropertyInfo propertyInfo = contentField.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				JType elementType = CodeModelUtil.getElementType(fieldVar.type());

				beanClazz._implements(iterableInterface.narrow(elementType));

				JMethod iteratorMethod = beanClazz.method(JMod.PUBLIC, iteratorInterface.narrow(elementType), "iterator");
				iteratorMethod.body()._return(JExpr.invoke("get" + propertyInfo.getName(true)).invoke("iterator"));
			}

			FieldOutline[] fields = clazz.getDeclaredFields();
			for(FieldOutline field : fields){
				CPropertyInfo propertyInfo = field.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				JMods modifiers = fieldVar.mods();
				if((modifiers.getValue() & JMod.PRIVATE) != JMod.PRIVATE){
					modifiers.setPrivate();
				} // End if

				if(propertyInfo.isCollection()){
					JFieldRef fieldRef = JExpr.refthis(propertyInfo.getName(false));

					JMethod hasElementsMethod = beanClazz.method(JMod.PUBLIC, boolean.class, "has" + propertyInfo.getName(true));
					hasElementsMethod.body()._return((fieldRef.ne(JExpr._null())).cand((fieldRef.invoke("size")).gt(JExpr.lit(0))));
				}
			}
		}

		return true;
	}

	static
	private FieldOutline getIdField(ClassOutline clazz){
		FieldFilter filter = new FieldFilter(){

			@Override
			public boolean accept(CPropertyInfo propertyInfo, JType type){
				return ("id").equals(propertyInfo.getName(false)) && ("java.lang.String").equals(type.fullName());
			}
		};

		return find(clazz, filter);
	}

	static
	private FieldOutline getExtensionsField(ClassOutline clazz){
		FieldFilter filter = new FieldFilter(){

			@Override
			public boolean accept(CPropertyInfo propertyInfo, JType type){

				if(("extensions").equals(propertyInfo.getName(false)) && propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(type);

					return ("org.dmg.pmml.Extension").equals(elementType.fullName());
				}

				return false;
			}
		};

		return find(clazz, filter);
	}

	static
	private FieldOutline getContentField(final ClassOutline clazz){
		FieldFilter filter = new FieldFilter(){

			private String name = clazz.implClass.name();


			@Override
			public boolean accept(CPropertyInfo propertyInfo, JType type){

				if(propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(type);

					String name = elementType.name();

					return ((this.name).equals(name + "s") || (this.name).equals(JJavaName.getPluralForm(name)));
				}

				return false;
			}
		};

		return find(clazz, filter);
	}

	static
	private FieldOutline find(ClassOutline clazz, FieldFilter filter){
		FieldOutline[] fields = clazz.getDeclaredFields();

		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();
			JType type = field.getRawType();

			if(filter.accept(propertyInfo, type)){
				return field;
			}
		}

		return null;
	}

	static
	private interface FieldFilter {

		boolean accept(CPropertyInfo propertyInfo, JType type);
	}
}