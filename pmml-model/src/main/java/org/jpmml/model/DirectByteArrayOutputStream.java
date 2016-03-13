/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

class DirectByteArrayOutputStream extends ByteArrayOutputStream {

	DirectByteArrayOutputStream(int capacity){
		super(capacity);
	}

	InputStream getInputStream(){
		return new ByteArrayInputStream(super.buf, 0, super.count);
	}
}