/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import com.beust.jcommander.*;

abstract
public class Example {

	abstract
	public void execute() throws Exception;

	static
	public <E extends Example> void execute(Class<? extends E> clazz, String... args) throws Exception {
		E example = clazz.newInstance();

		JCommander commander = new JCommander(example);
		commander.setProgramName(clazz.getName());

		try {
			commander.parse(args);
		} catch(ParameterException pe){
			commander.usage();

			System.exit(-1);
		}

		example.execute();
	}
}