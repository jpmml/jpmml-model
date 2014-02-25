/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.*;

public class SourceLocationNullifier extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		object.setSourceLocation(null);

		return VisitorAction.CONTINUE;
	}
}