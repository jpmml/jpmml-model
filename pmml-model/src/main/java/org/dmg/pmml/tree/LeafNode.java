/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.jpmml.model.annotations.Property;

public class LeafNode extends SimpleNode {

	private Object id = null;


	public LeafNode(){
	}

	public LeafNode(Node node){
		super(node);

		setId(node.getId());
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
}