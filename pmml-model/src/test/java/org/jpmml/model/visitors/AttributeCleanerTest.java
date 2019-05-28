/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.OutlierTreatmentMethod;
import org.dmg.pmml.Output;
import org.dmg.pmml.OutputField;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.model.ReflectionUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributeCleanerTest {

	@Test
	public void clean(){
		MiningField miningField = new MiningField()
			.setUsageType(MiningField.UsageType.ACTIVE)
			.setOutlierTreatment(OutlierTreatmentMethod.AS_MISSING_VALUES);

		assertEquals(MiningField.UsageType.ACTIVE, ReflectionUtil.getFieldValue(PMMLAttributes.MININGFIELD_USAGETYPE, miningField));
		assertEquals(OutlierTreatmentMethod.AS_MISSING_VALUES, ReflectionUtil.getFieldValue(PMMLAttributes.MININGFIELD_OUTLIERTREATMENT, miningField));

		MiningSchema miningSchema = new MiningSchema()
			.addMiningFields(miningField);

		OutputField outputField = new OutputField()
			.setFinalResult(Boolean.TRUE)
			.setIsMultiValued("0");

		assertEquals(Boolean.TRUE, ReflectionUtil.getFieldValue(PMMLAttributes.OUTPUTFIELD_FINALRESULT, outputField));
		assertEquals("0", ReflectionUtil.getFieldValue(PMMLAttributes.OUTPUTFIELD_ISMULTIVALUED, outputField));

		Output output = new Output()
			.addOutputFields(outputField);

		TreeModel treeModel = new TreeModel()
			.setScorable(Boolean.FALSE)
			.setMiningSchema(miningSchema)
			.setOutput(output);

		assertEquals(Boolean.FALSE, ReflectionUtil.getFieldValue(org.dmg.pmml.tree.PMMLAttributes.TREEMODEL_SCORABLE, treeModel));

		AttributeCleaner cleaner = new AttributeCleaner();
		cleaner.applyTo(treeModel);

		assertEquals((Enum<?>)null, ReflectionUtil.getFieldValue(PMMLAttributes.MININGFIELD_USAGETYPE, miningField));
		assertEquals(OutlierTreatmentMethod.AS_MISSING_VALUES, ReflectionUtil.getFieldValue(PMMLAttributes.MININGFIELD_OUTLIERTREATMENT, miningField));

		assertEquals(MiningField.UsageType.ACTIVE, miningField.getUsageType());
		assertEquals(OutlierTreatmentMethod.AS_MISSING_VALUES, miningField.getOutlierTreatment());

		assertEquals((Boolean)null, ReflectionUtil.getFieldValue(PMMLAttributes.OUTPUTFIELD_FINALRESULT, outputField));
		assertEquals((String)null, ReflectionUtil.getFieldValue(PMMLAttributes.OUTPUTFIELD_ISMULTIVALUED, outputField));

		assertEquals(Boolean.TRUE, outputField.isFinalResult());
		assertEquals("0", outputField.getIsMultiValued());

		assertEquals(Boolean.FALSE, ReflectionUtil.getFieldValue(org.dmg.pmml.tree.PMMLAttributes.TREEMODEL_SCORABLE, treeModel));
	}
}