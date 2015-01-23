/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.xml.sax.Locator;

/**
 * A visitors that clears the SAX Locator information of a class model object by setting it to <code>null</code>.
 *
 * @see PMMLObject#getLocator()
 * @see PMMLObject#setLocator(Locator)
 */
public class LocatorNullifier extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setLocator(null);

		return VisitorAction.CONTINUE;
	}
}