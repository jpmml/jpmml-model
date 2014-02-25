/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.*;

import org.dmg.pmml.*;

import org.junit.*;

import org.xml.sax.*;

public class SchemaUtilTest {

	@Test
	public void transformSource() throws Exception {
		Version[] versions = Version.values();

		for(Version version : versions){
			InputStream is = PMMLUtil.getResourceAsStream(version);

			try {
				InputSource source = new InputSource(is);

				JAXBUtil.unmarshalPMML(SchemaUtil.createImportSource(source));
			} finally {
				is.close();
			}
		}
	}
}