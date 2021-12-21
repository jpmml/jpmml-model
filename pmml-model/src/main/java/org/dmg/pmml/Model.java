/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Model extends PMMLObject {

	abstract
	public String getModelName();

	abstract
	public Model setModelName(String modelName);

	abstract
	public MiningFunction requireMiningFunction();

	abstract
	public MiningFunction getMiningFunction();

	abstract
	public Model setMiningFunction(MiningFunction miningFunction);

	abstract
	public String getAlgorithmName();

	abstract
	public Model setAlgorithmName(String algorithmName);

	abstract
	public boolean isScorable();

	abstract
	public Model setScorable(Boolean scorable);

	abstract
	public MathContext getMathContext();

	abstract
	public Model setMathContext(MathContext mathContext);

	abstract
	public MiningSchema requireMiningSchema();

	abstract
	public MiningSchema getMiningSchema();

	abstract
	public Model setMiningSchema(MiningSchema miningSchema);

	abstract
	public LocalTransformations getLocalTransformations();

	abstract
	public Model setLocalTransformations(LocalTransformations localTransformations);

	public Targets getTargets(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the {@link Targets} child element is not supported.
	 */
	public Model setTargets(Targets targets){
		throw new UnsupportedOperationException();
	}

	public Output getOutput(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the {@link Output} child element is not supported.
	 */
	public Model setOutput(Output output){
		throw new UnsupportedOperationException();
	}

	public ModelStats getModelStats(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the {@link ModelStats} child element is not supported.
	 */
	public Model setModelStats(ModelStats modelStats){
		throw new UnsupportedOperationException();
	}

	public ModelExplanation getModelExplanation(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the {@link ModelExplanation} child element is not supported.
	 */
	public Model setModelExplanation(ModelExplanation modelExplanation){
		throw new UnsupportedOperationException();
	}

	public ModelVerification getModelVerification(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the {@link ModelVerification} child element is not supported.
	 */
	public Model setModelVerification(ModelVerification modelVerification){
		throw new UnsupportedOperationException();
	}
}
