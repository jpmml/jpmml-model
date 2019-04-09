/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;

abstract
public class SimpleTransformer implements ClassFileTransformer {

	private ClassPool classPool = ClassPool.getDefault();


	abstract
	public boolean accept(String className);

	abstract
	public CtClass transform(CtClass ctClass) throws Exception;

	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if(accept(className)){
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
}