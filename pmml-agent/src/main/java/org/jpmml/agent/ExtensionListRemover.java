/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
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

public class ExtensionListRemover implements ClassFileTransformer {

	private ClassPool classPool = ClassPool.getDefault();


	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if(className.startsWith("org/dmg/pmml/")){
			className = className.replace('/', '.');

			this.classPool.insertClassPath(new ByteArrayClassPath(className, buffer));

			try {
				CtClass ctClass = this.classPool.get(className);

				ctClass = transform(ctClass);
				if(ctClass == null){
					return null;
				}

				return ctClass.toBytecode();
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		return null;
	}

	private CtClass transform(CtClass ctClass) throws CannotCompileException, NotFoundException {
		CtField extensionsField;

		try {
			extensionsField = ctClass.getDeclaredField("extensions", "Ljava/util/List;");
		} catch(NotFoundException nfe){
			return null;
		}

		ctClass.removeField(extensionsField);

		CtMethod hasExtensionsMethod = ctClass.getDeclaredMethod("hasExtensions");
		hasExtensionsMethod.setBody("return false;");

		CtMethod getExtensionsMethod = ctClass.getDeclaredMethod("getExtensions");
		getExtensionsMethod.setBody("throw new UnsupportedOperationException();");

		ClassFile classFile = ctClass.getClassFile();

		AnnotationsAttribute annotations = (AnnotationsAttribute)classFile.getAttribute(AnnotationsAttribute.visibleTag);

		Annotation xmlTypeAnnotation = annotations.getAnnotation("javax.xml.bind.annotation.XmlType");

		updatePropOrder((ArrayMemberValue)xmlTypeAnnotation.getMemberValue("propOrder"));

		annotations.addAnnotation(xmlTypeAnnotation);

		return ctClass;
	}

	static
	private void updatePropOrder(ArrayMemberValue propOrder){
		List<MemberValue> values = new ArrayList<>(Arrays.asList(propOrder.getValue()));

		Iterator<MemberValue> it = values.iterator();
		while(it.hasNext()){
			StringMemberValue value = (StringMemberValue)it.next();

			if("extensions".equals(value.getValue())){
				it.remove();
			}
		}

		propOrder.setValue(values.toArray(new MemberValue[values.size()]));
	}
}