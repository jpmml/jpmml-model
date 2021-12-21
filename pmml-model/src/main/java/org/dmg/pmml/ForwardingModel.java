/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
public class ForwardingModel extends Model {

	private Model model = null;


	private ForwardingModel(){
	}

	public ForwardingModel(Model model){
		setModel(model);
	}

	@Override
	public String getModelName(){
		return getModel().getModelName();
	}

	@Override
	public ForwardingModel setModelName(String modelName){
		getModel().setModelName(modelName);

		return this;
	}

	@Override
	public MiningFunction requireMiningFunction(){
		return getModel().requireMiningFunction();
	}

	@Override
	public MiningFunction getMiningFunction(){
		return getModel().getMiningFunction();
	}

	@Override
	public ForwardingModel setMiningFunction(MiningFunction miningFunction){
		getModel().setMiningFunction(miningFunction);

		return this;
	}

	@Override
	public String getAlgorithmName(){
		return getModel().getAlgorithmName();
	}

	@Override
	public ForwardingModel setAlgorithmName(String algorithmName){
		getModel().setAlgorithmName(algorithmName);

		return this;
	}

	@Override
	public boolean isScorable(){
		return getModel().isScorable();
	}

	@Override
	public ForwardingModel setScorable(Boolean scorable){
		getModel().setScorable(scorable);

		return this;
	}

	@Override
	public MathContext getMathContext(){
		return getModel().getMathContext();
	}

	@Override
	public ForwardingModel setMathContext(MathContext mathContext){
		getModel().setMathContext(mathContext);

		return this;
	}

	@Override
	public MiningSchema requireMiningSchema(){
		return getModel().requireMiningSchema();
	}

	@Override
	public MiningSchema getMiningSchema(){
		return getModel().getMiningSchema();
	}

	@Override
	public ForwardingModel setMiningSchema(MiningSchema miningSchema){
		getModel().setMiningSchema(miningSchema);

		return this;
	}

	@Override
	public LocalTransformations getLocalTransformations(){
		return getModel().getLocalTransformations();
	}

	@Override
	public ForwardingModel setLocalTransformations(LocalTransformations localTransformations){
		getModel().setLocalTransformations(localTransformations);

		return this;
	}

	@Override
	public Targets getTargets(){
		return getModel().getTargets();
	}

	@Override
	public ForwardingModel setTargets(Targets targets){
		getModel().setTargets(targets);

		return this;
	}

	@Override
	public Output getOutput(){
		return getModel().getOutput();
	}

	@Override
	public ForwardingModel setOutput(Output output){
		getModel().setOutput(output);

		return this;
	}

	@Override
	public ModelStats getModelStats(){
		return getModel().getModelStats();
	}

	@Override
	public ForwardingModel setModelStats(ModelStats modelStats){
		getModel().setModelStats(modelStats);

		return this;
	}

	@Override
	public ModelExplanation getModelExplanation(){
		return getModel().getModelExplanation();
	}

	@Override
	public ForwardingModel setModelExplanation(ModelExplanation modelExplanation){
		getModel().setModelExplanation(modelExplanation);

		return this;
	}

	@Override
	public ModelVerification getModelVerification(){
		return getModel().getModelVerification();
	}

	@Override
	public ForwardingModel setModelVerification(ModelVerification modelVerification){
		getModel().setModelVerification(modelVerification);

		return this;
	}

	@Override
	public VisitorAction accept(Visitor visitor){
		return getModel().accept(visitor);
	}

	public Model getModel(){
		return this.model;
	}

	private void setModel(Model model){
		this.model = Objects.requireNonNull(model);
	}
}