/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.DirectByteArrayOutputStream;

public class JacksonUtil {

	private JacksonUtil(){
	}

	static
	public PMML readPMML(InputStream is) throws IOException {
		return read(PMML.class, is);
	}

	static
	public <E extends PMMLObject> E read(Class<? extends E> clazz, InputStream is) throws IOException {
		ObjectMapper objectMapper = createObjectMapper(null);

		ObjectReader objectReader = objectMapper.readerFor(clazz);

		return objectReader.readValue(is);
	}

	static
	public void writePMML(PMML pmml, OutputStream os) throws IOException {
		write(pmml, os);
	}

	static
	public void write(PMMLObject object, OutputStream os) throws IOException {
		ObjectMapper objectMapper = createObjectMapper(null);

		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		objectWriter.writeValue(os, object);
	}

	static
	public <E extends PMMLObject> E clone(E object) throws JsonMappingException, IOException {
		ObjectMapper objectMapper = createObjectMapper(null);

		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(1024 * 1024);

		Class<?> clazz = object.getClass();

		ObjectWriter objectWriter = objectMapper.writerFor(clazz);

		try(OutputStream os = buffer){
			objectWriter.writeValue(os, object);
		}

		ObjectReader objectReader = objectMapper.readerFor(clazz);

		try(InputStream is = buffer.getInputStream()){
			return objectReader.readValue(is);
		}
	}

	static
	public ObjectMapper createObjectMapper(JsonFactory jsonFactory){
		ObjectMapper objectMapper = new ObjectMapper(jsonFactory)
			.enable(SerializationFeature.WRAP_ROOT_VALUE)
			.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

		objectMapper.registerModule(new PMMLModule());

		return objectMapper;
	}
}