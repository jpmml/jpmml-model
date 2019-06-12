/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.Predicate;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

public class LeafNode extends SimpleNode {

	private Object id = null;


	public LeafNode(){
	}

	@ValueConstructor
	public LeafNode(@Property("score") Object score, @Property("predicate") Predicate predicate){
		super(score, predicate);
	}

	@CopyConstructor
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