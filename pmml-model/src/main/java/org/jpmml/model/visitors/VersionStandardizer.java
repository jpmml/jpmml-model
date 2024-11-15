/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.MathContext;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.VisitorAction;

public class VersionStandardizer extends AbstractVisitor {

	@Override
	public VisitorAction visit(Model model){
		MathContext mathContext = model.getMathContext();

		if(mathContext != null){
			model.setMathContext(null);
		}

		return super.visit(model);
	}

	@Override
	public VisitorAction visit(PMML pmml){
		String baseVersion = pmml.getBaseVersion();

		if(baseVersion != null){
			pmml.setBaseVersion(null);
		}

		return super.visit(pmml);
	}
}