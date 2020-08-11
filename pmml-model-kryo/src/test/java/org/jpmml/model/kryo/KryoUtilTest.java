/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.dmg.pmml.InlineTableTest;
import org.dmg.pmml.PMML;
import org.jpmml.model.MixedContentTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

public class KryoUtilTest {

	private Kryo kryo = null;


	@Before
	public void setUp(){
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(false);
		kryo.addDefaultSerializer(Element.class, new JavaSerializer());

		KryoUtil.register(kryo);

		this.kryo = kryo;
	}

	@Test
	public void inlineTableTest() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(InlineTableTest.class);

		KryoUtil.clone(this.kryo, pmml);
	}

	@Test
	public void mixedContent() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(MixedContentTest.class);

		KryoUtil.clone(this.kryo, pmml);
	}
}