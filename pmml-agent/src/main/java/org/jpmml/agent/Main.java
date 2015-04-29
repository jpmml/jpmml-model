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
			options = options.replace(';', '\n');

			Reader reader = new StringReader(options);

			try {
				properties.load(reader);
			} finally {
				reader.close();
			}
		}

		String transform = properties.getProperty("transform");

		if(("true").equalsIgnoreCase(transform)){
			instrumentation.addTransformer(new PMMLObjectTransformer());
		}

		InstrumentationProvider.setInstrumentation(instrumentation);
	}
}