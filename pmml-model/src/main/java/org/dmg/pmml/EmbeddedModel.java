/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class EmbeddedModel extends PMMLObject {

	abstract
	public String getModelName();

	abstract
	public void setModelName(String modelName);

	abstract
	public MiningFunctionType getFunctionName();

	abstract
	public void setFunctionName(MiningFunctionType miningFunction);

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
}