/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.Version;
import org.dmg.pmml.adapters.ObjectAdapter;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.annotations.Added;
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
	"defaultChild",
	"predicate",
	"nodes"
})
public class BranchNode extends SimpleNode {

	@XmlAttribute(name = "id")
	@XmlJavaTypeAdapter(ObjectAdapter.class)
	@XmlSchemaType(name = "anySimpleType")
	@JsonProperty("id")
	private Object id = null;

	@XmlAttribute(name = "defaultChild")
	@XmlJavaTypeAdapter(ObjectAdapter.class)
	@XmlSchemaType(name = "anySimpleType")
	@Added(Version.PMML_3_1)
	@JsonProperty("defaultChild")
	private Object defaultChild = null;

	@XmlElements({
		@XmlElement(name = "Node", namespace = "http://www.dmg.org/PMML-4_4", type = ComplexNode.class)
	})
	@JsonProperty("Node")
	@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, defaultImpl = ComplexNode.class)
	private List<Node> nodes = null;


	public BranchNode(){
	}

	@ValueConstructor
	public BranchNode(@Property("score") Object score, @Property("predicate") Predicate predicate){
		super(score, predicate);
	}

	@CopyConstructor
	public BranchNode(Node node){
		super(node);

		setId(node.getId());
		setDefaultChild(node.getDefaultChild());

		if(node.hasNodes()){
			(getNodes()).addAll(node.getNodes());
		}
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

	@Override
	public Object requireDefaultChild(){

		if(this.defaultChild == null){
			throw new MissingAttributeException(this, PMMLAttributes.COMPLEXNODE_DEFAULTCHILD);
		}

		return this.defaultChild;
	}

	@Override
	public Object getDefaultChild(){
		return this.defaultChild;
	}

	@Override
	public BranchNode setDefaultChild(@Property("defaultChild") Object defaultChild){
		this.defaultChild = defaultChild;

		return this;
	}

	@Override
	public boolean hasNodes(){
		return (this.nodes != null) && (!this.nodes.isEmpty());
	}

	@Override
	public List<Node> getNodes(){

		if(this.nodes == null){
			this.nodes = new ArrayList<>();
		}

		return this.nodes;
	}
}