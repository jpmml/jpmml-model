/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.adapters.NumberAdapter;
import org.dmg.pmml.adapters.ObjectAdapter;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Optional;
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

	@XmlAttribute(name = "recordCount")
	@XmlJavaTypeAdapter(NumberAdapter.class)
	@Optional(org.dmg.pmml.Version.XPMML)
	@JsonProperty("recordCount")
	private Number recordCount;


	public SimpleScoreDistribution(){
	}

	@ValueConstructor
	public SimpleScoreDistribution(@Property("value") Object value, @Property("recordCount") Number recordCount){
		this.value = value;
		this.recordCount = recordCount;
	}

	@CopyConstructor
	public SimpleScoreDistribution(ScoreDistribution scoreDistribution){
		setValue(scoreDistribution.getValue());
		setRecordCount(scoreDistribution.getRecordCount());
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
	public Number requireRecordCount(){

		if(this.recordCount == null){
			throw new MissingAttributeException(this, PMMLAttributes.COMPLEXSCOREDISTRIBUTION_RECORDCOUNT);
		}

		return this.recordCount;
	}

	@Override
	public Number getRecordCount(){
		return this.recordCount;
	}

	@Override
	public SimpleScoreDistribution setRecordCount(@Property("recordCount") Number recordCount){
		this.recordCount = recordCount;

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