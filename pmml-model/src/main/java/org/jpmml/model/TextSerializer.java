/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.OutputStream;

import org.dmg.pmml.PMMLObject;

public interface TextSerializer extends Serializer {

	default
	void serializePretty(PMMLObject object, OutputStream os) throws Exception {
		serialize(object, os);
	}
}