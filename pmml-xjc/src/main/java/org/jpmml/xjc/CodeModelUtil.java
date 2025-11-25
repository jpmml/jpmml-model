/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.jpmml.xjc;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JGenerable;
import com.sun.codemodel.JType;

public class CodeModelUtil {

	private CodeModelUtil(){
	}

	static
	public JType getElementType(JType collectionType){
		JClass collectionClazz = (JClass)collectionType;

		List<JClass> elementTypes = collectionClazz.getTypeParameters();
		if(elementTypes.size() != 1){
			throw new IllegalArgumentException();
		}

		return elementTypes.get(0);
	}

	static
	public JAnnotationValue getAnnotationValue(JAnnotationUse annotation, String name){
		Map<String, JAnnotationValue> annotationMembers = annotation.getAnnotationMembers();

		if(!annotationMembers.containsKey(name)){
			throw new IllegalArgumentException(name);
		}

		return annotationMembers.get(name);
	}

	static
	public boolean hasAnnotation(Collection<JAnnotationUse> annotations, Class<?> clazz){
		JAnnotationUse annotation = findAnnotation(annotations, clazz);

		return (annotation != null);
	}

	static
	public JAnnotationUse findAnnotation(JAnnotatable annotatable, Class<?> clazz){
		List<JAnnotationUse> annotations = CodeModelUtil.getAnnotations(annotatable);

		return CodeModelUtil.findAnnotation(annotations, clazz);
	}

	static
	public JAnnotationUse findAnnotation(Collection<JAnnotationUse> annotations, Class<?> clazz){
		String fullName = clazz.getName();

		for(JAnnotationUse annotation : annotations){
			JClass type = annotation.getAnnotationClass();

			if((type.fullName()).equals(fullName)){
				return annotation;
			}
		}

		return null;
	}

	static
	public List<JAnnotationUse> getAnnotations(JAnnotatable annotatable){

		try {
			Class<?> clazz = annotatable.getClass();

			Field annotationsField;

			while(true){

				try {
					annotationsField = clazz.getDeclaredField("annotations");

					break;
				} catch(NoSuchFieldException nsfe){
					clazz = clazz.getSuperclass();

					if(clazz == null){
						throw nsfe;
					}
				}
			}

			ensureAccessible(annotationsField);

			return (List)annotationsField.get(annotatable);
		} catch(ReflectiveOperationException roe){
			throw new RuntimeException(roe);
		}
	}

	static
	public String stringValue(JGenerable generable){
		String result;

		try(StringWriter writer = new StringWriter()){
			generable.generate(new JFormatter(writer));

			result = writer.toString();
		} catch(IOException ioe){
			throw new RuntimeException(ioe);
		}

		if(result.length() >= 2 && (result.startsWith("\"") && result.endsWith("\""))){
			result = result.substring(1, result.length() - 1);
		} else

		{
			throw new RuntimeException();
		}

		return result;
	}

	@SuppressWarnings("deprecation")
	static
	void ensureAccessible(AccessibleObject accessibleObject){

		if(!accessibleObject.isAccessible()){
			accessibleObject.setAccessible(true);
		}
	}
}