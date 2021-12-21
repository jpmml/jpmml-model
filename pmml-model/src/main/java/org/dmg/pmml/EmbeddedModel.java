/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class EmbeddedModel extends PMMLObject {

	abstract
	public String getModelName();

	abstract
	public EmbeddedModel setModelName(String modelName);

	abstract
	public MiningFunction requireMiningFunction();

	abstract
	public MiningFunction getMiningFunction();

	abstract
	public EmbeddedModel setMiningFunction(MiningFunction miningFunction);

	abstract
	public String getAlgorithmName();

	abstract
	public EmbeddedModel setAlgorithmName(String algorithmName);

	abstract
	public LocalTransformations getLocalTransformations();

	abstract
	public EmbeddedModel setLocalTransformations(LocalTransformations localTransformations);

	abstract
	public Targets getTargets();

	abstract
	public EmbeddedModel setTargets(Targets targets);

	abstract
	public Output getOutput();

	abstract
	public EmbeddedModel setOutput(Output output);

	abstract
	public ModelStats getModelStats();

	abstract
	public EmbeddedModel setModelStats(ModelStats modelStats);
}