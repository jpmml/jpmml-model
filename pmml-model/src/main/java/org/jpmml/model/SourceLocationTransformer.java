/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.Serializable;

import com.sun.org.apache.xml.internal.utils.SerializableLocatorImpl;
import org.dmg.pmml.AbstractSimpleVisitor;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.xml.sax.Locator;

public class SourceLocationTransformer extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setSourceLocation(transform(object.sourceLocation()));

		return VisitorAction.CONTINUE;
	}

	private Locator transform(Locator locator){

		if((locator == null) || (locator instanceof Serializable)){
			return locator;
		}

		return new SerializableLocatorImpl(locator);
	}
}