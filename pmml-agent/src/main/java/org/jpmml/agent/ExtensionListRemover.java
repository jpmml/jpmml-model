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
import javassist.NotFoundException;

/**
 * <p>
 * A class file transformer that removes the <code>extensions</code> field declaration from all <code>org.dmg.pmml.HasExtensions</code> implementation classes.
 * </p>
 */
public class ExtensionListRemover implements ClassFileTransformer {

	private ClassPool classPool = ClassPool.getDefault();


	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if(className.startsWith("org/dmg/pmml/")){
			className = className.replace('/', '.');

			this.classPool.insertClassPath(new ByteArrayClassPath(className, buffer));

			try {
				CtClass ctClass = this.classPool.get(className);

				try {
					ctClass = transform(ctClass);
				} catch(NotFoundException nfe){
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
		TransformationUtil.removeElementList(ctClass, "extensions");

		return ctClass;
	}
}