/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * <p>
 * A class file transformer that removes the <code>locator</code> field declaration from the <code>org.dmg.pmml.PMMLObject</code> class.
 * </p>
 */
public class LocatorRemover implements ClassFileTransformer {

	private ClassPool classPool = ClassPool.getDefault();


	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if("org/dmg/pmml/PMMLObject".equals(className)){
			className = className.replace('/', '.');

			this.classPool.insertClassPath(new ByteArrayClassPath(className, buffer));

			try {
				CtClass ctClass = this.classPool.get(className);

				ctClass = transform(ctClass);

				return ctClass.toBytecode();
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		return null;
	}

	private CtClass transform(CtClass ctClass) throws CannotCompileException, NotFoundException {
		CtField field = ctClass.getDeclaredField("locator", "Lorg/xml/sax/Locator;");

		ctClass.removeField(field);

		CtMethod getterMethod = ctClass.getDeclaredMethod("getLocator");
		getterMethod.setBody(null);

		CtMethod setterMethod = ctClass.getDeclaredMethod("setLocator");
		setterMethod.setBody(null);

		return ctClass;
	}
}