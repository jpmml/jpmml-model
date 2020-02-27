/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Collection;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

public class CopyConstructorPlugin extends Plugin {

	@Override
	public String getOptionName(){
		return "XcopyConstructor";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		Model model = outline.getModel();

		JCodeModel codeModel = model.codeModel;

		JClass nodeClass = codeModel.ref("org.dmg.pmml.tree.Node");
		JClass copyConstructorAnnotation = codeModel.ref("org.jpmml.model.annotations.CopyConstructor");

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			if(!checkType(beanClazz, "org.dmg.pmml.tree.ComplexNode")){
				continue;
			}

			JMethod defaultConstructor = beanClazz.getConstructor(new JType[0]);
			if(defaultConstructor == null){
				defaultConstructor = beanClazz.constructor(JMod.PUBLIC);
			}

			JMethod copyConstructor = beanClazz.constructor(JMod.PUBLIC);
			copyConstructor.annotate(copyConstructorAnnotation);

			JVar nodeParam = copyConstructor.param(nodeClass, "node");

			JBlock body = copyConstructor.body();

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				String getterName = "get" + propertyInfo.getName(true);
				String setterName = "set" + propertyInfo.getName(true);

				if(propertyInfo.isCollection()){
					JBlock block = body._if(nodeParam.invoke("has" + propertyInfo.getName(true)))._then();

					block.add(JExpr.invoke(getterName).invoke("addAll").arg(nodeParam.invoke(getterName)));
				} else

				{
					JBlock block = body;

					block.add(JExpr.invoke(setterName).arg(nodeParam.invoke(getterName)));
				}
			}

			JMethod toComplexNodeMethod = beanClazz.method(JMod.PUBLIC, beanClazz, "toComplexNode");
			toComplexNodeMethod.annotate(Override.class);

			toComplexNodeMethod.body()._return(JExpr._this());
		}

		return true;
	}

	static
	private boolean checkType(JType type, String fullName){
		return (type.fullName()).equals(fullName);
	}
}