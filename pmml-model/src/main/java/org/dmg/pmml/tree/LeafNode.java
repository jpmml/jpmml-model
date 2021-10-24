/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.adapters.ObjectAdapter;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement(name = "Node", namespace = "http://www.dmg.org/PMML-4_4")
@XmlType(name = "", propOrder = {
	"predicate"
})
@JsonRootName("Node")
@JsonPropertyOrder({
	"id",
	"score",
	"predicate"
})
public class LeafNode extends SimpleNode {

	@XmlAttribute(name = "id")
	@XmlJavaTypeAdapter(ObjectAdapter.class)
	@XmlSchemaType(name = "anySimpleType")
	@JsonProperty("id")
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