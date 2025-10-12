/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class EmbeddedModel extends PMMLObject implements HasLocalTransformations<EmbeddedModel>, HasOutput<EmbeddedModel> {

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

	@Override
	abstract
	public LocalTransformations getLocalTransformations();

	@Override
	abstract
	public EmbeddedModel setLocalTransformations(LocalTransformations localTransformations);

	abstract
	public Targets getTargets();

	abstract
	public EmbeddedModel setTargets(Targets targets);

	@Override
	abstract
	public Output getOutput();

	@Override
	abstract
	public EmbeddedModel setOutput(Output output);

	abstract
	public ModelStats getModelStats();

	abstract
	public EmbeddedModel setModelStats(ModelStats modelStats);
}