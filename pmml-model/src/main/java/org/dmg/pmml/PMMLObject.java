/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import java.io.*;

import javax.xml.bind.annotation.*;

import com.sun.xml.bind.*;

import org.xml.sax.*;

@XmlTransient
abstract
public class PMMLObject implements Locatable, Serializable, Visitable {

	@Override
	abstract
	public Locator sourceLocation();

	abstract
	public void setSourceLocation(Locator locator);
}