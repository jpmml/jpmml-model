/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

import javassist.ByteArrayClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * <p>
 * A class file transformer that removes the selected field declarations from the <code>org.dmg.pmml.tree.Node</code> class.
 * </p>
 *
 * Transformer commands:
 * <ul>
 *   <li><code>simple</code> - Removes <code>partition</code> and <code>embeddedModel</code> field declarations.</li>
 *   <li><code>anonymous</code> - Removes <code>id</code> and <code>defaultChild</code> field declarations.</li>
 *   <li><code>regression</code> - Removes <code>recordCount</code> and <code>scoreDistributions</code> field declarations.</li>
 * </ul>
 */
public class NodeTransformer implements ClassFileTransformer {

	private Set<String> commands = null;

	private ClassPool classPool = ClassPool.getDefault();


	public NodeTransformer(Set<String> commands){
		setCommands(commands);
	}

	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if("org/dmg/pmml/tree/ComplexNode".equals(className)){
			className = className.replace('/', '.');

			this.classPool.insertClassPath(new ByteArrayClassPath(className, buffer));

			try {
				CtClass ctClass = this.classPool.get(className);

				if(ctClass.isFrozen()){
					ctClass.defrost();
				}

				ctClass = transform(ctClass);

				return ctClass.toBytecode();
			} catch(Exception e){
				throw new RuntimeException(e);
			}
		}

		return null;
	}

	private CtClass transform(CtClass ctClass) throws CannotCompileException, NotFoundException {
		Set<String> commands = getCommands();

		if(commands.contains("simple")){
			TransformationUtil.removeElement(ctClass, "partition");
			TransformationUtil.removeElement(ctClass, "embeddedModel");
		} // End if

		if(commands.contains("anonymous")){
			TransformationUtil.removeAttribute(ctClass, "id");
			TransformationUtil.removeAttribute(ctClass, "defaultChild");
		} // End if

		if(commands.contains("regression")){
			TransformationUtil.removeAttribute(ctClass, "recordCount");
			TransformationUtil.removeElementList(ctClass, "scoreDistributions");
		}

		return ctClass;
	}

	public Set<String> getCommands(){
		return this.commands;
	}

	private void setCommands(Set<String> commands){
		this.commands = commands;
	}
}