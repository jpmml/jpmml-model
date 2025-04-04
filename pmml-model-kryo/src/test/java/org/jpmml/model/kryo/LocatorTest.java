/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.resources.ResourceUtil;
import org.jpmml.model.visitors.LocatorNullifier;
import org.jpmml.model.visitors.LocatorTransformer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocatorTest extends KryoSerializerTest {

	@Test
	public void kryoClone() throws Exception {
		KryoSerializer kryoSerializer = new KryoSerializer(super.kryo);

		PMML pmml = ResourceUtil.unmarshal(Version.PMML_4_4);

		assertTrue(pmml.hasLocator());

		PMML clonedPmml = checkedClone(kryoSerializer, pmml);

		assertTrue(clonedPmml.hasLocator());

		pmml.accept(new LocatorTransformer());

		clonedPmml = checkedClone(kryoSerializer, pmml);

		assertTrue(clonedPmml.hasLocator());

		pmml.accept(new LocatorNullifier());

		assertFalse(pmml.hasLocator());

		clonedPmml = checkedClone(kryoSerializer, pmml);

		assertFalse(clonedPmml.hasLocator());
	}
}