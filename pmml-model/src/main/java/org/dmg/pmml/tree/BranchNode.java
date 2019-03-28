/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.ArrayList;
import java.util.List;

public class BranchNode extends SimpleNode {

	private Object id = null;

	private Object defaultChild = null;

	private List<Node> nodes = null;


	public BranchNode(){
	}

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
	public SimpleNode setId(Object id){
		this.id = id;

		return this;
	}

	@Override
	public Object getDefaultChild(){
		return this.defaultChild;
	}

	@Override
	public BranchNode setDefaultChild(Object defaultChild){
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