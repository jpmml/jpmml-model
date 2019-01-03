/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public class LeafNode extends SimpleNode {

	private String id = null;


	@Override
	public String getId(){
		return this.id;
	}

	@Override
	public SimpleNode setId(String id){
		this.id = id;

		return this;
	}
}