/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Model extends PMMLObject implements HasExtensions {

	abstract
	public String getModelName();

	abstract
	public Model setModelName(String modelName);

	abstract
	public MiningFunctionType getFunctionName();

	abstract
	public Model setFunctionName(MiningFunctionType functionName);

	abstract
	public String getAlgorithmName();

	abstract
	public Model setAlgorithmName(String algorithmName);

	abstract
	public boolean isScorable();

	abstract
	public Model setScorable(Boolean scorable);

	abstract
	public MiningSchema getMiningSchema();

	abstract
	public Model setMiningSchema(MiningSchema miningSchema);

	abstract
	public LocalTransformations getLocalTransformations();

	abstract
	public Model setLocalTransformations(LocalTransformations localTransformations);

	/**
	 * @throws UnsupportedOperationException If the {@link Targets} child element is not supported.
	 * This is typically the case with unsupervised learning models.
	 */
	abstract
	public Targets getTargets();

	/**
	 * @throws UnsupportedOperationException If the {@link Targets} child element is not supported.
	 */
	abstract
	public Model setTargets(Targets targets);

	/**
	 * @throws UnsupportedOperationException If the {@link Output} child element is not supported.
	 */
	abstract
	public Output getOutput();

	/**
	 * @throws UnsupportedOperationException If the {@link Output} child element is not supported.
	 */
	abstract
	public Model setOutput(Output output);

	abstract
	public ModelStats getModelStats();

	abstract
	public Model setModelStats(ModelStats modelStats);

	/**
	 * @throws UnsupportedOperationException If the {@link ModelExplanation} child element is not supported.
	 */
	abstract
	public ModelExplanation getModelExplanation();

	/**
	 * @throws UnsupportedOperationException If the {@link ModelExplanation} child element is not supported.
	 */
	abstract
	public Model setModelExplanation(ModelExplanation modelExplanation);

	/**
	 * @throws UnsupportedOperationException If the {@link ModelVerification} child element is not supported.
	 */
	abstract
	public ModelVerification getModelVerification();

	/**
	 * @throws UnsupportedOperationException If the {@link ModelVerification} child element is not supported.
	 */
	abstract
	public Model setModelVerification(ModelVerification modelVerification);
}