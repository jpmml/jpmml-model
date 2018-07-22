/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.xml.sax.Locator;

/**
 * <p>
 * A Visitor that clears the SAX Locator information of a class model object by setting it to <code>null</code>.
 * </p>
 *
 * @see PMMLObject#getLocator()
 * @see PMMLObject#setLocator(Locator)
 */
public class LocatorNullifier extends AbstractVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setLocator(null);

		return super.visit(object);
	}
}