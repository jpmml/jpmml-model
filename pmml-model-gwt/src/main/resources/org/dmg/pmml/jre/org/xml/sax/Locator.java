/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.xml.sax;

public interface Locator {

	String getPublicId();

	String getSystemId();

	int getLineNumber();

	int getColumnNumber();
}
