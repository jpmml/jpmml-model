/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.Serializable;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.AbstractSimpleVisitor;
import org.xml.sax.Locator;

public class SourceLocationTransformer extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setLocator(transform(object.getLocator()));

		return VisitorAction.CONTINUE;
	}

	private Locator transform(Locator locator){

		if((locator == null) || (locator instanceof Serializable)){
			return locator;
		}

		return new SimpleLocator(locator);
	}
}