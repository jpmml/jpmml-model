/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.jpmml.xjc;

import com.sun.tools.xjc.outline.FieldOutline;

public interface FieldFilter {

	boolean accept(FieldOutline fieldOutline);
}