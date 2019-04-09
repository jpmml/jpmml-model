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

			try(Reader reader = new StringReader(options)){
				properties.load(reader);
			}
		}

		String locator = properties.getProperty("locator");
		if(locator != null && ("false").equalsIgnoreCase(locator)){
			instrumentation.addTransformer(new LocatorRemover(), true);
		}

		String extensions = properties.getProperty("extensions");
		if(extensions != null && ("false").equalsIgnoreCase(extensions)){
			instrumentation.addTransformer(new ExtensionListRemover(), true);
		}

		String publicize = properties.getProperty("public");
		if(publicize != null && ("true").equalsIgnoreCase(publicize)){
			instrumentation.addTransformer(new FieldPublicizer(), true);
		}

		InstrumentationProvider.setInstrumentation(instrumentation);
	}
}