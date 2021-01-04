/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.io.Serializable;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.junit.Test;
import org.xml.sax.Locator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocatorTest extends KryoUtilTest {

	@Test
	public void kryoClone() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		pmml = clone(pmml);

		Locator locator = pmml.getLocator();

		assertNotNull(locator);
		assertFalse(locator instanceof Serializable);

		LocatorTransformer locatorTransformer = new LocatorTransformer();
		locatorTransformer.applyTo(pmml);

		pmml = clone(pmml);

		locator = pmml.getLocator();

		assertTrue(locator instanceof Serializable);

		LocatorNullifier locatorNullifier = new LocatorNullifier();
		locatorNullifier.applyTo(pmml);

		pmml = clone(pmml);

		locator = pmml.getLocator();

		assertNull(locator);
	}
}