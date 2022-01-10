/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.adapters.ObjectAdapter;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.MissingElementException;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlTransient
abstract
public class SimpleNode extends Node {

	@XmlAttribute(name = "score")
	@XmlJavaTypeAdapter(ObjectAdapter.class)
	@XmlSchemaType(name = "anySimpleType")
	@JsonProperty("score")
	private Object score = null;

	@XmlElements({
		@XmlElement(name = "SimplePredicate", namespace = "http://www.dmg.org/PMML-4_4", type = org.dmg.pmml.SimplePredicate.class),
		@XmlElement(name = "CompoundPredicate", namespace = "http://www.dmg.org/PMML-4_4", type = org.dmg.pmml.CompoundPredicate.class),
		@XmlElement(name = "SimpleSetPredicate", namespace = "http://www.dmg.org/PMML-4_4", type = org.dmg.pmml.SimpleSetPredicate.class),
		@XmlElement(name = "True", namespace = "http://www.dmg.org/PMML-4_4", type = org.dmg.pmml.True.class),
		@XmlElement(name = "False", namespace = "http://www.dmg.org/PMML-4_4", type = org.dmg.pmml.False.class)
	})
	@JsonProperty("Predicate")
	@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
	@JsonSubTypes({
		@JsonSubTypes.Type(name = "SimplePredicate", value = org.dmg.pmml.SimplePredicate.class),
		@JsonSubTypes.Type(name = "CompoundPredicate", value = org.dmg.pmml.CompoundPredicate.class),
		@JsonSubTypes.Type(name = "SimpleSetPredicate", value = org.dmg.pmml.SimpleSetPredicate.class),
		@JsonSubTypes.Type(name = "True", value = org.dmg.pmml.True.class),
		@JsonSubTypes.Type(name = "False", value = org.dmg.pmml.False.class)
	})
	private Predicate predicate = null;


	public SimpleNode(){
	}

	@ValueConstructor
	public SimpleNode(@Property("score") Object score, @Property("predicate") Predicate predicate){
		this.score = score;
		this.predicate = predicate;
	}

	@CopyConstructor
	public SimpleNode(Node node){
		setScore(node.getScore());
		setPredicate(node.getPredicate());
	}

	@Override
	public Object requireScore(){

		if(this.score == null){
			throw new MissingAttributeException(this, PMMLAttributes.COMPLEXNODE_SCORE);
		}

		return this.score;
	}

	@Override
	public Object getScore(){
		return this.score;
	}

	@Override
	public SimpleNode setScore(@Property("score") Object score){
		this.score = score;

		return this;
	}

	@Override
	public Predicate requirePredicate(){

		if(this.predicate == null){
			throw new MissingElementException(this, PMMLElements.COMPLEXNODE_PREDICATE);
		}

		return this.predicate;
	}

	@Override
	public Predicate getPredicate(){
		return this.predicate;
	}

	@Override
	public SimpleNode setPredicate(@Property("predicate") Predicate predicate){
		this.predicate = predicate;

		return this;
	}

	@Override
	public VisitorAction accept(Visitor visitor){
		VisitorAction status = visitor.visit(this);

		if(status == VisitorAction.CONTINUE){
			visitor.pushParent(this);

			if(status == VisitorAction.CONTINUE && hasExtensions()){
				status = PMMLObject.traverse(visitor, getExtensions());
			} // End if

			if(status == VisitorAction.CONTINUE){
				status = PMMLObject.traverse(visitor, getPredicate(), getPartition());
			} // End if

			if(status == VisitorAction.CONTINUE && hasScoreDistributions()){
				status = PMMLObject.traverse(visitor, getScoreDistributions());
			} // End if

			if(status == VisitorAction.CONTINUE && hasNodes()){
				status = PMMLObject.traverse(visitor, getNodes());
			} // End if

			if(status == VisitorAction.CONTINUE){
				status = PMMLObject.traverse(visitor, getEmbeddedModel());
			}

			visitor.popParent();
		} // End if

		if(status == VisitorAction.TERMINATE){
			return VisitorAction.TERMINATE;
		}

		return VisitorAction.CONTINUE;
	}
}