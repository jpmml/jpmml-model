/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import org.xml.sax.Locator;

public interface HasLocator {

	boolean hasLocator();

	Locator getLocator();

	void setLocator(Locator locator);
}