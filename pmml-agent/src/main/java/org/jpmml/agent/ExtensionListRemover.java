/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * <p>
 * A class file transformer that removes the <code>extensions</code> field declaration from all <code>org.dmg.pmml.HasExtensions</code> implementation classes.
 * </p>
 */
public class ExtensionListRemover extends SimpleTransformer {

	@Override
	public boolean accept(String className){
		return className.startsWith("org/dmg/pmml/");
	}

	@Override
	public CtClass transform(CtClass ctClass) throws CannotCompileException {

		try {
			TransformationUtil.removeElementList(ctClass, "extensions");
		} catch(NotFoundException nfe){
			return null;
		}

		return ctClass;
	}
}