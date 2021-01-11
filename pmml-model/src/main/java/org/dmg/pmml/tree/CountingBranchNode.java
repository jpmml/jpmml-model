/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.adapters.NumberAdapter;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement(name = "Node", namespace = "http://www.dmg.org/PMML-4_4")
@XmlType(name = "", propOrder = {
	"predicate",
	"nodes"
})
@JsonRootName("Node")
@JsonPropertyOrder({
	"id",
	"score",
	"recordCount",
	"defaultChild",
	"predicate",
	"nodes"
})
public class CountingBranchNode extends BranchNode {

	@XmlAttribute(name = "recordCount")
	@XmlJavaTypeAdapter(NumberAdapter.class)
	@JsonProperty("recordCount")
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