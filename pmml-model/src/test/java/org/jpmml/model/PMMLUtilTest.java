/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Version;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PMMLUtilTest {

	@Test
	public void unmarshal() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){

			if(!version.isStandard()){
				continue;
			}

			PMML pmml;

			try(InputStream is = ResourceUtil.getStream(version)){
				pmml = PMMLUtil.unmarshal(is);
			}

			assertEquals(pmml.requireVersion(), Version.PMML_4_4.getVersion());
			assertEquals(pmml.getBaseVersion(), version.getVersion());
		}
	}
}