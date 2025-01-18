/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.UnmarshalException;
import org.junit.Test;

import static org.junit.Assert.fail;

public class XEEAttackTest {

	@Test
	public void unmarshal() throws Exception {

		try(InputStream is = ResourceUtil.getStream(XEEAttackTest.class)){
			JAXBSerializer serializer = new JAXBSerializer();

			Source source = new StreamSource(is);

			serializer.unmarshal(source);

			fail();
		} catch(UnmarshalException ue){
			// Ignored
		}
	}
}