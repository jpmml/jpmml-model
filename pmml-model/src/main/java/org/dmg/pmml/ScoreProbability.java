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
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement(name = "ScoreDistribution", namespace = "http://www.dmg.org/PMML-4_4")
@XmlType(name = "")
@JsonRootName("ScoreDistribution")
@JsonPropertyOrder({
	"value",
	"recordCount",
	"probability"
})
public class ScoreProbability extends SimpleScoreDistribution {

	@XmlAttribute(name = "probability")
	@XmlJavaTypeAdapter(NumberAdapter.class)
	@JsonProperty("probability")
	private Number probability;


	public ScoreProbability(){
	}

	@ValueConstructor
	public ScoreProbability(@Property("value") Object value, @Property("recordCount") Number recordCount, @Property("probability") Number probability){
		super(value, recordCount);

		this.probability = probability;
	}

	@CopyConstructor
	public ScoreProbability(ScoreDistribution scoreDistribution){
		super(scoreDistribution);

		setProbability(scoreDistribution.getProbability());
	}

	@Override
	public Number requireProbability(){

		if(this.probability == null){
			throw new MissingAttributeException(this, PMMLAttributes.COMPLEXSCOREDISTRIBUTION_PROBABILITY);
		}

		return this.probability;
	}

	@Override
	public Number getProbability(){
		return this.probability;
	}

	@Override
	public ScoreProbability setProbability(@Property("probability") Number probability){
		this.probability = probability;

		return this;
	}
}