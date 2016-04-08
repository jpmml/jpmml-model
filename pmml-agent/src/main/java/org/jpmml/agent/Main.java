/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

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
		if(locator != null && !("true").equalsIgnoreCase(locator)){
			instrumentation.addTransformer(new LocatorRemover(), true);
		}

		String extensions = properties.getProperty("extensions");
		if(extensions != null && !("true").equalsIgnoreCase(extensions)){
			instrumentation.addTransformer(new ExtensionListRemover(), true);
		}

		String node = properties.getProperty("node");
		if(node != null){
			Set<String> commands = new LinkedHashSet<>(Arrays.asList(node.split(",")));

			instrumentation.addTransformer(new NodeTransformer(commands), true);
		}

		InstrumentationProvider.setInstrumentation(instrumentation);
	}
}