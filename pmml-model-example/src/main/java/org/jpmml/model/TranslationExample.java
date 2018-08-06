/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.dmg.pmml.PMML;

public class TranslationExample extends Example {

	@Parameter (
		names = {"--input"},
		description = "Input PMML file",
		required = true
	)
	private File input = null;

	@Parameter (
		names = {"--output"},
		description = "Output JSON file",
		required = true
	)
	private File output = null;

	@Parameter (
		names = {"--indent"},
		description = "Indent output"
	)
	private boolean indent = false;


	static
	public void main(String... args) throws Exception {
		execute(TranslationExample.class, args);
	}

	@Override
	public void execute() throws Exception {
		PMML pmml = unmarshalPMML(this.input);

		JsonFactory jsonFactory = null;

		String name = this.output.getName();
		String nameExtension = null;

		int dot = name.lastIndexOf('.');
		if(dot > -1){
			nameExtension = name.substring(dot + 1);
		} // End if

		if(("YAML").equalsIgnoreCase(nameExtension)){
			jsonFactory = new YAMLFactory();
		}

		ObjectMapper mapper = new ObjectMapper(jsonFactory);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, this.indent);

		PMMLModule pmmlModule = new PMMLModule();
		mapper.registerModule(pmmlModule);

		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

		try(OutputStream os = new FileOutputStream(this.output)){
			mapper.writeValue(os, pmml);
		}
	}
}