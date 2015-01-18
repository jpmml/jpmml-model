/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.Serializable;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.xml.sax.Locator;

/**
 * A visitor that transforms the SAX Locator information of a class model object to {@link Serializable} form.
 *
 * @see PMMLObject#getLocator()
 * @see PMMLObject#setLocator(Locator)
 */
public class SourceLocationTransformer extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setLocator(transform(object.getLocator()));

		return VisitorAction.CONTINUE;
	}

	static
	private Locator transform(Locator locator){

		if((locator == null) || (locator instanceof Serializable)){
			return locator;
		}

		return new SimpleLocator(locator);
	}
}