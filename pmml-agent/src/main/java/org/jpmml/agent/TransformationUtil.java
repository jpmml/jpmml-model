/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class TransformationUtil {

	private TransformationUtil(){
	}

	static
	public void removeAttribute(CtClass ctClass, String name) throws CannotCompileException, NotFoundException {
		removeField(ctClass, name);
	}

	static
	public void removeElement(CtClass ctClass, String name) throws CannotCompileException, NotFoundException {
		removeField(ctClass, name);

		updatePropOrder(ctClass, name);
	}

	static
	public void removeElementList(CtClass ctClass, String name) throws CannotCompileException, NotFoundException {
		removeListField(ctClass, name);

		updatePropOrder(ctClass, name);
	}

	static
	private void removeField(CtClass ctClass, String name) throws CannotCompileException, NotFoundException {
		CtField field = ctClass.getDeclaredField(name);

		ctClass.removeField(field);

		CtMethod getterMethod = ctClass.getDeclaredMethod(formatMethodName("get", name));
		getterMethod.setBody(null);

		CtMethod setterMethod = ctClass.getDeclaredMethod(formatMethodName("set", name));
		setterMethod.setBody("throw new UnsupportedOperationException();");
	}

	static
	private void removeListField(CtClass ctClass, String name) throws CannotCompileException, NotFoundException {
		CtField field = ctClass.getDeclaredField(name, "Ljava/util/List;");

		ctClass.removeField(field);

		CtMethod testMethod = ctClass.getDeclaredMethod(formatMethodName("has", name));
		testMethod.setBody("return false;");

		CtMethod getterMethod = ctClass.getDeclaredMethod(formatMethodName("get", name));
		getterMethod.setBody("throw new UnsupportedOperationException();");
	}

	static
	private void updatePropOrder(CtClass ctClass, String name){
		ClassFile classFile = ctClass.getClassFile();

		AnnotationsAttribute annotations = (AnnotationsAttribute)classFile.getAttribute(AnnotationsAttribute.visibleTag);

		Annotation xmlTypeAnnotation = annotations.getAnnotation("jakarta.xml.bind.annotation.XmlType");

		ArrayMemberValue propOrderValue = (ArrayMemberValue)xmlTypeAnnotation.getMemberValue("propOrder");

		removeValue(propOrderValue, name);

		annotations.addAnnotation(xmlTypeAnnotation);
	}

	static
	private void removeValue(ArrayMemberValue memberValue, String value){
		List<MemberValue> values = new ArrayList<>(Arrays.asList(memberValue.getValue()));

		boolean removed = false;

		Iterator<MemberValue> it = values.iterator();
		while(it.hasNext()){
			StringMemberValue stringValue = (StringMemberValue)it.next();

			if((value).equals(stringValue.getValue())){
				it.remove();

				removed = true;
			}
		}

		if(!removed){
			throw new RuntimeException(value + " not in " + values);
		}

		memberValue.setValue(values.toArray(new MemberValue[values.size()]));
	}

	static
	private String formatMethodName(String prefix, String name){
		return prefix + (name.substring(0, 1)).toUpperCase() + name.substring(1);
	}
}