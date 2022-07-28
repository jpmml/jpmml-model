/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.adapters.NumberAdapter;
import org.jpmml.model.MissingAttributeException;
import org.jpmml.model.annotations.CopyConstructor;
import org.jpmml.model.annotations.Optional;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement(name = "ScoreDistribution", namespace = "http://www.dmg.org/PMML-4_4")
@XmlType(name = "")
@JsonRootName("ScoreDistribution")
@JsonPropertyOrder({
	"value",
	"recordCount"
})
public class ScoreFrequency extends SimpleScoreDistribution {

	@XmlAttribute(name = "recordCount")
	@XmlJavaTypeAdapter(NumberAdapter.class)
	@Optional(org.dmg.pmml.Version.XPMML)
	@JsonProperty("recordCount")
	private Number recordCount;


	public ScoreFrequency(){
	}

	@ValueConstructor
	public ScoreFrequency(@Property("value") Object value, @Property("recordCount") Number recordCount){
		super(value);

		this.recordCount = recordCount;
	}

	@CopyConstructor
	public ScoreFrequency(ScoreDistribution scoreDistribution){
		super(scoreDistribution);

		setRecordCount(scoreDistribution.getRecordCount());
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
}