/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.jpmml.model.TextSerializer;

public class JacksonSerializer implements TextSerializer {

	private ObjectMapper objectMapper = null;

	private Class<? extends PMMLObject> rootType;


	public JacksonSerializer(ObjectMapper objectMapper){
		this(objectMapper, PMML.class);
	}

	public JacksonSerializer(ObjectMapper objectMapper, Class<? extends PMMLObject> rootType){
		setObjectMapper(objectMapper);
		setRootType(rootType);
	}

	@Override
	public PMMLObject deserialize(InputStream is) throws IOException {
		ObjectMapper objectMapper = getObjectMapper();
		Class<? extends PMMLObject> rootType = getRootType();

		ObjectReader objectReader = objectMapper.readerFor(rootType);

		return objectReader.readValue(is);
	}

	@Override
	public void serialize(PMMLObject object, OutputStream os) throws IOException {
		ObjectMapper objectMapper = getObjectMapper();
		Class<? extends PMMLObject> rootType = getRootType();

		ObjectWriter objectWriter = objectMapper.writerFor(rootType);

		objectWriter.writeValue(os, object);
	}

	@Override
	public void serializePretty(PMMLObject object, OutputStream os) throws IOException {
		ObjectMapper objectMapper = getObjectMapper();
		Class<? extends PMMLObject> rootType = getRootType();

		DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter("\t", "\n");

		DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
		prettyPrinter.indentObjectsWith(indenter);
		prettyPrinter.indentArraysWith(indenter);

		ObjectWriter objectWriter = objectMapper.writerFor(rootType)
			.with(prettyPrinter);

		objectWriter.writeValue(os, object);
	}

	public ObjectMapper getObjectMapper(){
		return this.objectMapper;
	}

	private void setObjectMapper(ObjectMapper objectMapper){
		this.objectMapper = Objects.requireNonNull(objectMapper);
	}

	public Class<? extends PMMLObject> getRootType(){
		return this.rootType;
	}

	private void setRootType(Class<? extends PMMLObject> rootType){
		this.rootType = Objects.requireNonNull(rootType);
	}
}