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
	public void setModelName(String modelName);

	abstract
	public MiningFunctionType getFunctionName();

	abstract
	public void setFunctionName(MiningFunctionType functionName);

	abstract
	public String getAlgorithmName();

	abstract
	public void setAlgorithmName(String algorithmName);

	abstract
	public boolean isScorable();

	abstract
	public void setScorable(Boolean scorable);

	abstract
	public MiningSchema getMiningSchema();

	abstract
	public void setMiningSchema(MiningSchema miningSchema);

	abstract
	public LocalTransformations getLocalTransformations();

	abstract
	public void setLocalTransformations(LocalTransformations localTransformations);

	abstract
	public Targets getTargets();

	abstract
	public void setTargets(Targets targets);

	abstract
	public Output getOutput();

	abstract
	public void setOutput(Output output);

	abstract
	public ModelStats getModelStats();

	abstract
	public void setModelStats(ModelStats modelStats);

	abstract
	public ModelExplanation getModelExplanation();

	abstract
	public void setModelExplanation(ModelExplanation modelExplanation);

	abstract
	public ModelVerification getModelVerification();

	abstract
	public void setModelVerification(ModelVerification modelVerification);
}