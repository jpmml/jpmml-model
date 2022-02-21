/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.lang.reflect.Field;
import java.util.Collection;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocCommentable;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

public class NoJavadocPlugin extends Plugin {

	@Override
	public String getOptionName(){
		return "Xno-javadoc";
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
			JDefinedClass beanClazz = classOutline.implClass;

			nullifyJavadoc(beanClazz);
		}

		Collection<? extends EnumOutline> enumOutlines = outline.getEnums();
		for(EnumOutline enumOutline : enumOutlines){
			JDefinedClass clazz = enumOutline.clazz;

			nullifyJavadoc(clazz);
		}

		return true;
	}

	static
	private void nullifyJavadoc(JDefinedClass clazz){
		nullifyJavadoc((JDocCommentable)clazz);

		Collection<JMethod> methods = clazz.methods();
		for(JMethod method : methods){
			nullifyJavadoc(method);
		}
	}

	@SuppressWarnings("deprecation")
	static
	private void nullifyJavadoc(JDocCommentable commentable){
		Class<?> clazz = commentable.getClass();

		try {
			Field jdocField = clazz.getDeclaredField("jdoc");
			if(!jdocField.isAccessible()){
				jdocField.setAccessible(true);
			}

			jdocField.set(commentable, null);
		} catch(ReflectiveOperationException roe){
			throw new RuntimeException(roe);
		}
	}
}