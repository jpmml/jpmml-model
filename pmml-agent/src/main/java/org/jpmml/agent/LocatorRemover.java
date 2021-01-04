/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * <p>
 * A class file transformer that removes the <code>locator</code> field declaration from the <code>org.dmg.pmml.PMMLObject</code> class.
 * </p>
 */
public class LocatorRemover extends SimpleTransformer {

	@Override
	public boolean accept(String className){
		return className.equals("org/dmg/pmml/PMMLObject");
	}

	@Override
	public CtClass transform(CtClass ctClass) throws CannotCompileException, NotFoundException {
		CtField field = ctClass.getDeclaredField("locator", "Lorg/xml/sax/Locator;");

		ctClass.removeField(field);

		CtMethod checkerMethod = ctClass.getDeclaredMethod("hasLocator");
		checkerMethod.setBody("return false;");

		CtMethod getterMethod = ctClass.getDeclaredMethod("getLocator");
		getterMethod.setBody(null);

		CtMethod setterMethod = ctClass.getDeclaredMethod("setLocator");
		setterMethod.setBody(null);

		return ctClass;
	}
}