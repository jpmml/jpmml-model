/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public class CountingBranchNode extends BranchNode {

	private Number recordCount = null;


	public CountingBranchNode(){
	}

	public CountingBranchNode(Node node){
		super(node);

		setRecordCount(node.getRecordCount());
	}

	@Override
	public Number getRecordCount(){
		return this.recordCount;
	}

	@Override
	public CountingBranchNode setRecordCount(Number recordCount){
		this.recordCount = recordCount;

		return this;
	}
}