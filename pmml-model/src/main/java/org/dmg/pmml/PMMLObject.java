/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.bind.Locatable;
import org.xml.sax.Locator;

@XmlTransient
abstract
public class PMMLObject implements Locatable, Serializable, Visitable {

	@Override
	abstract
	public Locator sourceLocation();

	abstract
	public void setSourceLocation(Locator locator);
}