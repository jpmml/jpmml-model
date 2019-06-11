/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;

abstract
public class SimpleNode extends Node {

	private Object score = null;

	private Predicate predicate = null;


	public SimpleNode(){
	}

	@CopyConstructor
	public SimpleNode(Node node){
		setScore(node.getScore());
		setPredicate(node.getPredicate());
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