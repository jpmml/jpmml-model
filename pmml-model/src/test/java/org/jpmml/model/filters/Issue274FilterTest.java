/*
 * Copyright (c) 2022 Villu Ruusmann
 */
package org.jpmml.model.filters;

import jakarta.xml.bind.UnmarshalException;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.fail;

public class Issue274FilterTest {

	@Test
	public void filterProtocol() throws Exception {

		try {
			ResourceUtil.unmarshal(Issue274FilterTest.class);

			fail();
		} catch(UnmarshalException ue){
			// Ignored
		}

		ResourceUtil.unmarshal(Issue274FilterTest.class, new Issue274Filter());
	}
}