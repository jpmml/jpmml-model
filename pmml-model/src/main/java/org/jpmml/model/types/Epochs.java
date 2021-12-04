/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.types;

import java.time.LocalDate;

public interface Epochs {

	LocalDate YEAR_1960 = LocalDate.of(1960, 1, 1);
	LocalDate YEAR_1970 = LocalDate.of(1970, 1, 1);
	LocalDate YEAR_1980 = LocalDate.of(1980, 1, 1);

	LocalDate YEAR_1990 = LocalDate.of(1990, 1, 1);
	LocalDate YEAR_2000 = LocalDate.of(2000, 1, 1);
	LocalDate YEAR_2010 = LocalDate.of(2010, 1, 1);
	LocalDate YEAR_2020 = LocalDate.of(2020, 1, 1);
}