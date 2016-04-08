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
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class NodeTransformer implements ClassFileTransformer {

	private Set<String> commands = null;

	private ClassPool classPool = ClassPool.getDefault();


	public NodeTransformer(Set<String> commands){
		setCommands(commands);
	}

	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz, ProtectionDomain protectionDomain, byte[] buffer) throws IllegalClassFormatException {

		if("org/dmg/pmml/Node".equals(className)){
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

		if(commands.contains("double-score")){
			updateScoreType(ctClass, "java.lang.Double");
		} else

		if(commands.contains("float-score")){
			updateScoreType(ctClass, "java.lang.Float");
		} // End if

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

	private void updateScoreType(CtClass ctClass, String type) throws CannotCompileException, NotFoundException {
		CtField field = ctClass.getDeclaredField("score", "Ljava/lang/String;");

		CtClass typeClass = this.classPool.get(type);

		field.setType(typeClass);

		CtMethod getterMethod = ctClass.getDeclaredMethod("getScore");
		getterMethod.setBody("return (this.score != null ? this.score.toString() : null);");

		CtMethod setterMethod = ctClass.getDeclaredMethod("setScore");
		setterMethod.setBody("{this.score = ($1 != null ? new " + type + "($1) : null); return this;}");
	}

	public Set<String> getCommands(){
		return this.commands;
	}

	private void setCommands(Set<String> commands){
		this.commands = commands;
	}
}