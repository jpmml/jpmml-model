/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;

public class SourceLocationNullifier extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setLocator(null);

		return VisitorAction.CONTINUE;
	}
}