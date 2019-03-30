/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public class CountingBranchNode extends BranchNode {

	private Double recordCount = null;


	public CountingBranchNode(){
	}

	public CountingBranchNode(Node node){
		super(node);

		setRecordCount(node.getRecordCount());
	}

	@Override
	public Double getRecordCount(){
		return this.recordCount;
	}

	@Override
	public CountingBranchNode setRecordCount(Double recordCount){
		this.recordCount = recordCount;

		return this;
	}
}