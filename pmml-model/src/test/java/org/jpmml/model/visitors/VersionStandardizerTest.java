/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.MathContext;
import org.dmg.pmml.PMML;
import org.dmg.pmml.tree.PMMLAttributes;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.model.ReflectionUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VersionStandardizerTest {

	@Test
	public void standardizePMML(){
		PMML pmml = new PMML()
			.setVersion("4.4")
			.setBaseVersion("4.3");

		TreeModel treeModel = new TreeModel()
			.setMathContext(MathContext.FLOAT);

		pmml.addModels(treeModel);

		VersionStandardizer inspector = new VersionStandardizer();
		inspector.applyTo(pmml);

		assertNotNull(pmml.getVersion());
		assertNull(pmml.getBaseVersion());

		// The getter method is returning the default field value
		assertEquals(MathContext.DOUBLE, treeModel.getMathContext());

		assertNull(ReflectionUtil.getFieldValue(PMMLAttributes.TREEMODEL_MATHCONTEXT, treeModel));
	}
}