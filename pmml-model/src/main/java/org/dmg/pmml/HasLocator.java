/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.dmg.pmml;

import org.xml.sax.Locator;

public interface HasLocator {

	Locator getLocator();

	void setLocator(Locator locator);
}