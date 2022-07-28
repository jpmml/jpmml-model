/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.adapters.ObjectAdapter;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlTransient
abstract
public class SimpleScoreDistribution extends ScoreDistribution {

	@XmlAttribute(name = "value", required = true)
	@XmlJavaTypeAdapter(ObjectAdapter.class)
	@XmlSchemaType(name = "anySimpleType")
	@JsonProperty("value")
	private Object value;


	public SimpleScoreDistribution(){
	}

	@ValueConstructor
	public SimpleScoreDistribution(@Property("value") Object value){
		this.value = value;
	}

	@CopyConstructor
	public SimpleScoreDistribution(ScoreDistribution scoreDistribution){
		setValue(scoreDistribution.getValue());
	}

	@Override
	public Object requireValue(){

		if(this.value == null){
			throw new MissingAttributeException(this, PMMLAttributes.COMPLEXSCOREDISTRIBUTION_VALUE);
		}

		return this.value;
	}

	@Override
	public Object getValue(){
		return this.value;
	}

	@Override
	public SimpleScoreDistribution setValue(@Property("value") Object value){
		this.value = value;

		return this;
	}

	@Override
	public VisitorAction accept(Visitor visitor){
		VisitorAction status = visitor.visit(this);

		if(status == VisitorAction.CONTINUE){
			visitor.pushParent(this);

			if(status == VisitorAction.CONTINUE && hasExtensions()){
				status = PMMLObject.traverse(visitor, getExtensions());
			}

			visitor.popParent();
		} // End if

		if(status == VisitorAction.TERMINATE){
			return VisitorAction.TERMINATE;
		}

		return VisitorAction.CONTINUE;
	}
}