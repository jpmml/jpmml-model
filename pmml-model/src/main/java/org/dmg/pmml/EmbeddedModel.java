/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class EmbeddedModel extends PMMLObject implements HasExtensions {

	abstract
	public String getModelName();

	abstract
	public EmbeddedModel setModelName(String modelName);

	abstract
	public MiningFunctionType getMiningFunction();

	abstract
	public EmbeddedModel setMiningFunction(MiningFunctionType miningFunction);

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