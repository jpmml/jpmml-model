/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.ArrayList;
import java.util.List;

import org.dmg.pmml.ScoreDistribution;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;

public class ClassifierNode extends SimpleNode {

	private Object id = null;

	private Number recordCount = null;

	private Object defaultChild = null;

	private List<ScoreDistribution> scoreDistributions = null;

	private List<Node> nodes = null;


	public ClassifierNode(){
	}

	@CopyConstructor
	public ClassifierNode(Node node){
		super(node);

		setId(node.getId());
		setRecordCount(node.getRecordCount());
		setDefaultChild(node.getDefaultChild());

		if(node.hasScoreDistributions()){
			(getScoreDistributions()).addAll(node.getScoreDistributions());
		} // End if

		if(node.hasNodes()){
			(getNodes()).addAll(node.getNodes());
		}
	}

	@Override
	public Object getId(){
		return this.id;
	}

	@Override
	public ClassifierNode setId(@Property("id") Object id){
		this.id = id;

		return this;
	}

	@Override
	public Number getRecordCount(){
		return this.recordCount;
	}

	@Override
	public ClassifierNode setRecordCount(@Property("recordCount") Number recordCount){
		this.recordCount = recordCount;

		return this;
	}

	@Override
	public Object getDefaultChild(){
		return this.defaultChild;
	}

	@Override
	public ClassifierNode setDefaultChild(@Property("defaultChild") Object defaultChild){
		this.defaultChild = defaultChild;

		return this;
	}

	@Override
	public boolean hasScoreDistributions(){
		return (this.scoreDistributions != null) && (this.scoreDistributions.size() > 0);
	}

	@Override
	public List<ScoreDistribution> getScoreDistributions(){

		if(this.scoreDistributions == null){
			this.scoreDistributions = new ArrayList<>();
		}

		return this.scoreDistributions;
	}

	@Override
	public boolean hasNodes(){
		return (this.nodes != null) && (this.nodes.size() > 0);
	}

	@Override
	public List<Node> getNodes(){

		if(this.nodes == null){
			this.nodes = new ArrayList<>();
		}

		return this.nodes;
	}
}