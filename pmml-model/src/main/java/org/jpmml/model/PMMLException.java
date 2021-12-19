/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;
import org.xml.sax.Locator;

abstract
public class PMMLException extends RuntimeException {

	private PMMLObject context = null;


	public PMMLException(String message){
		super(message);
	}

	public PMMLException(String message, PMMLObject context){
		super(message);

		setContext(context);
	}

	@Override
	synchronized
	public PMMLException initCause(Throwable throwable){
		return (PMMLException)super.initCause(throwable);
	}

	public PMMLException ensureContext(PMMLObject parentContext){
		PMMLObject context = getContext();

		if(context == null){
			setContext(parentContext);
		}

		return this;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		Class<? extends PMMLException> clazz = getClass();
		sb.append(clazz.getName());

		PMMLObject context = getContext();
		if(context != null){
			int lineNumber = -1;

			Locator locator = context.getLocator();
			if(locator != null){
				lineNumber = locator.getLineNumber();
			} // End if

			if(lineNumber != -1){
				sb.append(" ").append("(at or around line ").append(lineNumber).append(" of the PMML document)");
			}
		}

		String message = getLocalizedMessage();
		if(message != null){
			sb.append(":");

			sb.append(" ").append(message);
		}

		return sb.toString();
	}

	public PMMLObject getContext(){
		return this.context;
	}

	private void setContext(PMMLObject context){
		this.context = context;
	}
}