/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.Serializable;

import org.xml.sax.Locator;

class SimpleLocator implements Locator, Serializable {

	private String publicId = null;

	private String systemId = null;

	private int lineNumber = -1;

	private int columnNumber = -1;


	private SimpleLocator(){
	}

	public SimpleLocator(Locator locator){
		setPublicId(locator.getPublicId());
		setSystemId(locator.getSystemId());
		setLineNumber(locator.getLineNumber());
		setColumnNumber(locator.getColumnNumber());
	}

	@Override
	public String getPublicId(){
		return this.publicId;
	}

	private void setPublicId(String publicId){
		this.publicId = publicId;
	}

	@Override
	public String getSystemId(){
		return this.systemId;
	}

	private void setSystemId(String systemId){
		this.systemId = systemId;
	}

	@Override
	public int getLineNumber(){
		return this.lineNumber;
	}

	private void setLineNumber(int lineNumber){
		this.lineNumber = lineNumber;
	}

	@Override
	public int getColumnNumber(){
		return this.columnNumber;
	}

	private void setColumnNumber(int columnNumber){
		this.columnNumber = columnNumber;
	}
}