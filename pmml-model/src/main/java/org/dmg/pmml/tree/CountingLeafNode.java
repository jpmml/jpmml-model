/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public class CountingLeafNode extends LeafNode {

	private Double recordCount = null;


	public CountingLeafNode(){
	}

	public CountingLeafNode(Node node){
		super(node);

		setRecordCount(node.getRecordCount());
	}

	@Override
	public Double getRecordCount(){
		return this.recordCount;
	}

	@Override
	public CountingLeafNode setRecordCount(Double recordCount){
		this.recordCount = recordCount;

		return this;
	}
}