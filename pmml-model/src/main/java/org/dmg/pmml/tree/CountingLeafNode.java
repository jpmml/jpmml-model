/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;

public class CountingLeafNode extends LeafNode {

	private Number recordCount = null;


	public CountingLeafNode(){
	}

	@CopyConstructor
	public CountingLeafNode(Node node){
		super(node);

		setRecordCount(node.getRecordCount());
	}

	@Override
	public Number getRecordCount(){
		return this.recordCount;
	}

	@Override
	public CountingLeafNode setRecordCount(@Property("recordCount") Number recordCount){
		this.recordCount = recordCount;

		return this;
	}
}