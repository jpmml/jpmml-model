/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml;

import java.util.List;

/**
 * <p>
 * A marker interface for PMML elements that specify {@link Extension} child elements.
 * </p>
 */
public interface HasExtensions<E extends PMMLObject & HasExtensions<E>> {

	boolean hasExtensions();

	List<Extension> getExtensions();

	E addExtensions(Extension... extensions);
}
