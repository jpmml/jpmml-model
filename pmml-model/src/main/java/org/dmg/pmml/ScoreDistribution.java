/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Arrays;
import java.util.List;

import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.adapters.ScoreDistributionAdapter;

@XmlTransient
@XmlJavaTypeAdapter (
	value = ScoreDistributionAdapter.class
)
abstract
public class ScoreDistribution extends PMMLObject implements HasRecordCount<ScoreDistribution> {

	public ComplexScoreDistribution toComplexScoreDistribution(){
		return new ComplexScoreDistribution(this);
	}

	public Object requireValue(){
		throw new UnsupportedOperationException();
	}

	public Object getValue(){
		return null;
	}

	public ScoreDistribution setValue(Object value){
		throw new UnsupportedOperationException();
	}

	public Number requireRecordCount(){
		throw new UnsupportedOperationException();
	}

	@Override
	public Number getRecordCount(){
		return null;
	}

	@Override
	public ScoreDistribution setRecordCount(Number recordCount){
		throw new UnsupportedOperationException();
	}

	public Number getConfidence(){
		return null;
	}

	public ScoreDistribution setConfidence(Number confidence){
		throw new UnsupportedOperationException();
	}

	public Number requireProbability(){
		throw new UnsupportedOperationException();
	}

	public Number getProbability(){
		return null;
	}

	public ScoreDistribution setProbability(Number probability){
		throw new UnsupportedOperationException();
	}

	public boolean hasExtensions(){
		return false;
	}

	public List<Extension> getExtensions(){
		throw new UnsupportedOperationException();
	}

	public ScoreDistribution addExtensions(Extension... extensions){
		getExtensions().addAll(Arrays.asList(extensions));

		return this;
	}
}