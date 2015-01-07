/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package com.sun.xml.bind;

import org.xml.sax.Locator;

public interface Locatable {

	Locator sourceLocation();
}