/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import org.xml.sax.Locator;

@XmlTransient
abstract
public class PMMLObject implements HasLocator, Serializable, Visitable {

	@XmlTransient
	@com.sun.xml.bind.annotation.XmlLocation
	@org.eclipse.persistence.oxm.annotations.XmlLocation
	private Locator locator = null;


	@Override
	public Locator getLocator(){
		return this.locator;
	}

	@Override
	public void setLocator(Locator locator){
		this.locator = locator;
	}
}