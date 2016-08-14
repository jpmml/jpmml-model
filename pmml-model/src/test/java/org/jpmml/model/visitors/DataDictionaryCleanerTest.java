/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.jpmml.model.ChainedSegmentationTest;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.FieldUtil;
import org.jpmml.model.NestedSegmentationTest;
import org.jpmml.model.ResourceUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataDictionaryCleanerTest {

	@Test
	public void cleanChained() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(ChainedSegmentationTest.class);

		DataDictionary dataDictionary = pmml.getDataDictionary();

		checkFields(FieldNameUtil.create("y", "x1", "x2", "x3", "x4"), dataDictionary.getDataFields());

		DataDictionaryCleaner cleaner = new DataDictionaryCleaner();
		cleaner.applyTo(pmml);

		checkFields(FieldNameUtil.create("y", "x1", "x2", "x3"), dataDictionary.getDataFields());

		List<Model> models = pmml.getModels();
		models.clear();

		cleaner.applyTo(pmml);

		checkFields(Collections.<FieldName>emptySet(), dataDictionary.getDataFields());
	}

	@Test
	public void cleanNested() throws Exception {
		PMML pmml = ResourceUtil.unmarshal(NestedSegmentationTest.class);

		DataDictionary dataDictionary = pmml.getDataDictionary();

		checkFields(FieldNameUtil.create("y", "x1", "x2", "x3", "x4", "x5"), dataDictionary.getDataFields());

		DataDictionaryCleaner cleaner = new DataDictionaryCleaner();
		cleaner.applyTo(pmml);

		checkFields(FieldNameUtil.create("x1", "x2", "x3", "x4", "x5"), dataDictionary.getDataFields());

		List<Model> models = pmml.getModels();
		models.clear();

		cleaner.applyTo(pmml);

		checkFields(Collections.<FieldName>emptySet(), dataDictionary.getDataFields());
	}

	static
	private void checkFields(Set<FieldName> names, Collection<DataField> dataFields){
		assertEquals(names, FieldUtil.nameSet(dataFields));
	}
}