/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.Serializable;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.xml.sax.Locator;

/**
 * <p>
 * A Visitor that transforms the SAX Locator information of a class model object to {@link Serializable} form.
 * </p>
 *
 * @see PMMLObject#getLocator()
 * @see PMMLObject#setLocator(Locator)
 */
public class LocatorTransformer extends AbstractVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setLocator(transform(object.getLocator()));

		return super.visit(object);
	}

	static
	private Locator transform(Locator locator){

		if((locator == null) || (locator instanceof Serializable)){
			return locator;
		}

		return new SimpleLocator(locator);
	}
}