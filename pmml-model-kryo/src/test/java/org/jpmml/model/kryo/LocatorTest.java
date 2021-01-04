/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.dmg.pmml.Visitor;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocatorTest extends KryoUtilTest {

	@Test
	public void kryoClone() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		pmml = updateLocator(pmml, null);

		assertTrue(pmml.hasLocator());

		pmml = updateLocator(pmml, new LocatorTransformer());

		assertTrue(pmml.hasLocator());

		pmml = updateLocator(pmml, new LocatorNullifier());

		assertFalse(pmml.hasLocator());
	}

	private PMML updateLocator(PMML pmml, Visitor visitor){

		if(visitor != null){
			pmml.accept(visitor);
		}

		return clone(pmml);
	}
}