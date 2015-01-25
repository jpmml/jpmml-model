/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

import java.util.List;

public interface HasExtensions {

	boolean hasExtensions();

	List<Extension> getExtensions();
}
