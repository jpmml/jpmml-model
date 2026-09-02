/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.List;

public interface HasScores<E extends PMMLObject & HasScores<E>> {

	boolean hasScores();

	List<Score> getScores();

	E addScores(Score... scores);
}