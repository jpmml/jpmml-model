/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.plugin;

import java.io.IOException;
import java.io.OutputStream;

import org.dmg.pmml.PMML;
import org.jpmml.model.SerializationUtil;
import org.jpmml.model.jackson.JacksonUtil;

public enum Format {

	JSON(){

		@Override
		public void write(PMML pmml, OutputStream os) throws IOException {
			JacksonUtil.writePMML(pmml, os);
		}
	},

	SER(){

		@Override
		public void write(PMML pmml, OutputStream os) throws IOException {
			SerializationUtil.serializePMML(pmml, os);
		}
	};


	private
	Format(){
	}

	abstract
	public void write(PMML pmml, OutputStream os) throws Exception;
}