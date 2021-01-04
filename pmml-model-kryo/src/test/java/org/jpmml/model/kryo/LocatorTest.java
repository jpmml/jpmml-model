/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.io.Serializable;

import com.esotericsoftware.kryo.Kryo;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Locator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocatorTest {

	private Kryo kryo = null;


	@Before
	public void setUp(){
		Kryo kryo = new Kryo();

		KryoUtil.init(kryo);
		KryoUtil.register(kryo);

		this.kryo = kryo;
	}

	@Test
	public void kryoClone() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		pmml = KryoUtil.clone(this.kryo, pmml);

		Locator locator = pmml.getLocator();

		assertNotNull(locator);
		assertFalse(locator instanceof Serializable);

		LocatorTransformer locatorTransformer = new LocatorTransformer();
		locatorTransformer.applyTo(pmml);

		pmml = KryoUtil.clone(this.kryo, pmml);

		locator = pmml.getLocator();

		assertTrue(locator instanceof Serializable);

		LocatorNullifier locatorNullifier = new LocatorNullifier();
		locatorNullifier.applyTo(pmml);

		pmml = KryoUtil.clone(this.kryo, pmml);

		locator = pmml.getLocator();

		assertNull(locator);
	}
}