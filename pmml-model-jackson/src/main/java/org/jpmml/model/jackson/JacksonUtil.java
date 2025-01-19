/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonUtil {

	private JacksonUtil(){
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