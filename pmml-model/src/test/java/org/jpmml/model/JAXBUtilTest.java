/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.validation.Schema;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JAXBUtilTest {

	@Test
	public void hasSchema() throws Exception {
		Schema schema = JAXBUtil.getSchema();

		assertNotNull(schema);
	}
}