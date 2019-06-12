/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.ArrayList;
import java.util.List;

import org.dmg.pmml.Predicate;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

public class BranchNode extends SimpleNode {

	private Object id = null;

	private Object defaultChild = null;

	private List<Node> nodes = null;


	public BranchNode(){
	}

	@ValueConstructor
	public BranchNode(@Property("score") Object score, @Property("predicate") Predicate predicate){
		super(score, predicate);
	}

	@CopyConstructor
	public BranchNode(Node node){
		super(node);

		setId(node.getId());
		setDefaultChild(node.getDefaultChild());

		if(node.hasNodes()){
			(getNodes()).addAll(node.getNodes());
		}
	}

	@Override
	public Object getId(){
		return this.id;
	}

	@Override
	public SimpleNode setId(@Property("id") Object id){
		this.id = id;

		return this;
	}

	@Override
	public Object getDefaultChild(){
		return this.defaultChild;
	}

	@Override
	public BranchNode setDefaultChild(@Property("defaultChild") Object defaultChild){
		this.defaultChild = defaultChild;

		return this;
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