/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import java.io.*;

import com.sun.org.apache.xml.internal.utils.*;

import org.xml.sax.*;

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