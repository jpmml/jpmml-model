/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.Predicate;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

public class CountingLeafNode extends LeafNode {

	private Number recordCount = null;


	public CountingLeafNode(){
	}

	@ValueConstructor
	public CountingLeafNode(@Property("score") Object score, @Property("predicate") Predicate predicate){
		super(score, predicate);
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