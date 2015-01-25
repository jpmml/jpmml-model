/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.agent;

import java.lang.instrument.Instrumentation;

public class InstrumentationProvider {

	private InstrumentationProvider(){
	}

	static
	public Instrumentation getInstrumentation(){
		return InstrumentationProvider.instrumentation;
	}

	static
	void setInstrumentation(Instrumentation instrumentation){
		InstrumentationProvider.instrumentation = instrumentation;
	}

	private static Instrumentation instrumentation = null;
}