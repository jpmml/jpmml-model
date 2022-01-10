/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasScoreDistributions<E extends PMMLObject & HasScoreDistributions<E>> extends HasScore<E> {

	Object requireScore();

	boolean hasScoreDistributions();

	List<ScoreDistribution> getScoreDistributions();

	E addScoreDistributions(ScoreDistribution... scoreDistributions);
}