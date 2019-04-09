/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.agent;

import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;

/**
 * <p>
 * A class file transformer that relaxes the visibility of all instance fields from <code>private</code> to <code>public</code>.
 * </p>
 */
public class FieldPublicizer extends SimpleTransformer {

	@Override
	public boolean accept(String className){
		return className.startsWith("org/dmg/pmml/");
	}

	@Override
	public CtClass transform(CtClass ctClass){
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