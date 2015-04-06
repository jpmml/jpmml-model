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

	abstract
	public Targets getTargets();

	abstract
	public Model setTargets(Targets targets);

	abstract
	public Output getOutput();

	abstract
	public Model setOutput(Output output);

	abstract
	public ModelStats getModelStats();

	abstract
	public Model setModelStats(ModelStats modelStats);

	abstract
	public ModelExplanation getModelExplanation();

	abstract
	public Model setModelExplanation(ModelExplanation modelExplanation);

	abstract
	public ModelVerification getModelVerification();

	abstract
	public Model setModelVerification(ModelVerification modelVerification);
}