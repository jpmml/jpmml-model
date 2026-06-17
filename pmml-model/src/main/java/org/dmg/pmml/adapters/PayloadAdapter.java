/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.dmg.pmml.ComplexScoreDistribution;
import org.dmg.pmml.Payload;
import org.dmg.pmml.ScoreDistribution;

public class PayloadAdapter extends XmlAdapter<Payload, Payload> {

	@Override
	public Payload unmarshal(Payload value){

		if(value instanceof ComplexScoreDistribution){
			ComplexScoreDistribution complexScoreDistribution = (ComplexScoreDistribution)value;

			return PayloadAdapter.SCOREDISTRIBUTION_ADAPTER.unmarshal(complexScoreDistribution);
		}

		return value;
	}

	@Override
	public Payload marshal(Payload payload){

		if(payload instanceof ScoreDistribution){
			ScoreDistribution scoreDistribution = (ScoreDistribution)payload;

			return PayloadAdapter.SCOREDISTRIBUTION_ADAPTER.marshal(scoreDistribution);
		}

		return payload;
	}

	private static final ScoreDistributionAdapter SCOREDISTRIBUTION_ADAPTER = new ScoreDistributionAdapter();
}