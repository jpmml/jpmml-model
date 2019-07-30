/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
		description = "Indent string"
	)
	private String indent = null;


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

		DefaultPrettyPrinter prettyPrinter = null;

		if(this.indent != null){
			DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter(filterIndent(this.indent), "\n");

			prettyPrinter = new DefaultPrettyPrinter();
			prettyPrinter.indentObjectsWith(indenter);
			prettyPrinter.indentArraysWith(indenter);
		}

		ObjectMapper mapper = JacksonUtil.createObjectMapper(jsonFactory);

		try(OutputStream os = new FileOutputStream(this.output)){
			ObjectWriter writer = mapper.writer(prettyPrinter);

			writer.writeValue(os, pmml);
		}
	}

	static
	private String filterIndent(String indent){

		if(("\\t").equals(indent)){
			return "\t";
		}

		return indent;
	}
}