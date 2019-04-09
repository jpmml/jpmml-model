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
import javassist.CtField;
import javassist.Modifier;

/**
 * <p>
 * A class file transformer that relaxes the visibility of all instance fields from <code>private</code> to <code>public</code>.
 * </p>
 */
public class FieldPublicizer implements ClassFileTransformer {

	private ClassPool classPool = ClassPool.getDefault();


	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if(className.startsWith("org/dmg/pmml/")){
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

	private CtClass transform(CtClass ctClass){
		CtField[] fields = ctClass.getDeclaredFields();

		for(CtField field : fields){
			int modifiers = field.getModifiers();

			if(!Modifier.isStatic(modifiers) && !Modifier.isPublic(modifiers)){
				modifiers = Modifier.setPublic(modifiers);

				field.setModifiers(modifiers);
			}
		}

		return ctClass;
	}
}