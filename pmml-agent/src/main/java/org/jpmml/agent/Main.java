/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.instrument.Instrumentation;
import java.util.Properties;

public class Main {

	static
	public void premain(String options, Instrumentation instrumentation) throws IOException {
		Properties properties = new Properties();

		if(options != null){
			options = options.replace(',', '\n');

			Reader reader = new StringReader(options);

			try {
				properties.load(reader);
			} finally {
				reader.close();
			}
		}

		String optimize = properties.getProperty("optimize");

		if(("true").equalsIgnoreCase(optimize)){
			instrumentation.addTransformer(new PMMLObjectTransformer());
		}
	}
}