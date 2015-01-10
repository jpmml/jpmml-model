/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;

@XmlTransient
abstract
public class PMMLObject implements HasLocator, Serializable, Visitable {

	@XmlLocation
	@XmlTransient
	protected Locator locator = null;


	@Override
	public Locator getLocator(){
		return this.locator;
	}

	@Override
	public void setLocator(Locator locator){
		this.locator = locator;
	}
}