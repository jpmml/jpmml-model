/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.Predicate;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

public class CountingBranchNode extends BranchNode {

	private Number recordCount = null;


	public CountingBranchNode(){
	}

	@ValueConstructor
	public CountingBranchNode(@Property("score") Object score, @Property("predicate") Predicate predicate){
		super(score, predicate);
	}

	@CopyConstructor
	public CountingBranchNode(Node node){
		super(node);

		setRecordCount(node.getRecordCount());
	}

	@Override
	public Number getRecordCount(){
		return this.recordCount;
	}

	@Override
	public CountingBranchNode setRecordCount(@Property("recordCount") Number recordCount){
		this.recordCount = recordCount;

		return this;
	}
}