/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import org.dmg.pmml.Version;

public class PMMLOutputStream extends FilterOutputStream {

	private Version version = null;

	private ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);


	public PMMLOutputStream(OutputStream os, Version version){
		super(os);

		this.version = Objects.requireNonNull(version);

		if(!version.isStandard()){
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void write(byte[] bytes) throws IOException {
		this.write(bytes, 0, bytes.length);
	}

	@Override
	public void write(byte[] bytes, int offset, int length) throws IOException {

		if(this.buffer != null){

			for(int i = offset, max = offset + length; i < max; i++){
				this.write(bytes[i]);
			}
		} else

		{
			super.out.write(bytes, offset, length);
		}
	}

	@Override
	public void write(int b) throws IOException {

		if(this.buffer != null){
			this.buffer.write(b);

			if(b == '>'){
				String string = this.buffer.toString("UTF-8");

				if(string.endsWith("?>")){
					super.out.write(string.getBytes("UTF-8"));

					this.buffer.reset();
				} else

				if(string.endsWith(">")){
					string = string.replace(Version.PMML_4_4.getNamespaceURI(), this.version.getNamespaceURI());

					super.out.write(string.getBytes("UTF-8"));

					this.buffer = null;
				} else

				{
					throw new IllegalStateException();
				}
			}
		} else

		{
			super.out.write(b);
		}
	}

	@Override
	public void flush() throws IOException {

		if(this.buffer != null){
			throw new IllegalStateException();
		}

		super.flush();
	}

	@Override
	public void close() throws IOException {
		super.close();
	}
}