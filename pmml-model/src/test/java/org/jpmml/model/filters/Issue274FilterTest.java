/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.filters;

import jakarta.xml.bind.UnmarshalException;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Issue274FilterTest {

	@Test
	public void filterProtocol() throws Exception {
		assertThrows(UnmarshalException.class, () -> ResourceUtil.unmarshal(Issue274FilterTest.class));

		ResourceUtil.unmarshal(Issue274FilterTest.class, new Issue274Filter());
	}
}