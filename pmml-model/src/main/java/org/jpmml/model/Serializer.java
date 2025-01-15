/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.io.OutputStream;

import org.dmg.pmml.PMMLObject;

public interface Serializer {

	PMMLObject deserialize(InputStream is) throws Exception;

	void serialize(PMMLObject object, OutputStream os) throws Exception;
}