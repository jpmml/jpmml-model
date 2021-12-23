/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.DataField;
import org.dmg.pmml.IntSparseArray;
import org.dmg.pmml.PMMLAttributes;
import org.dmg.pmml.PMMLElements;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XPathUtilTest {

	@Test
	public void formatComparisonMeasure(){
		assertEquals("ComparisonMeasure/<Measure>", XPathUtil.formatElementOrAttribute(PMMLElements.COMPARISONMEASURE_MEASURE));
	}

	@Test
	public void formatCompoundPredicate(){
		assertEquals("CompoundPredicate/<Predicate>", XPathUtil.formatElementOrAttribute(PMMLElements.COMPOUNDPREDICATE_PREDICATES));
	}

	@Test
	public void formatDataField() throws Exception {
		assertEquals("DataField", XPathUtil.formatElement(DataField.class));

		assertEquals("DataField@name", XPathUtil.formatElementOrAttribute(PMMLAttributes.DATAFIELD_NAME));
		assertEquals("DataField/Extension", XPathUtil.formatElementOrAttribute(PMMLElements.DATAFIELD_EXTENSIONS));
		assertEquals("DataField/Interval", XPathUtil.formatElementOrAttribute(PMMLElements.DATAFIELD_INTERVALS));
		assertEquals("DataField/Value", XPathUtil.formatElementOrAttribute(PMMLElements.DATAFIELD_VALUES));

		assertEquals("DataField@isCyclic", XPathUtil.formatAttribute(PMMLAttributes.DATAFIELD_CYCLIC, null));
		assertEquals("DataField@isCyclic=0", XPathUtil.formatAttribute(PMMLAttributes.DATAFIELD_CYCLIC, "0"));
	}

	@Test
	public void formatDerivedField(){
		assertEquals("DerivedField/<Expression>", XPathUtil.formatElementOrAttribute(PMMLElements.DERIVEDFIELD_EXPRESSION));
	}

	@Test
	public void formatPMML(){
		assertEquals("PMML/<Model>", XPathUtil.formatElementOrAttribute(PMMLElements.PMML_MODELS));
	}

	@Test
	public void formatSegment(){
		assertEquals("Segment/<Model>", XPathUtil.formatElementOrAttribute(org.dmg.pmml.mining.PMMLElements.SEGMENT_MODEL));
		assertEquals("Segment/<Predicate>", XPathUtil.formatElementOrAttribute(org.dmg.pmml.mining.PMMLElements.SEGMENT_PREDICATE));
	}

	@Test
	public void formatSparseArray(){
		assertEquals("INT-SparseArray", XPathUtil.formatElement(IntSparseArray.class));

		assertEquals("INT-SparseArray@defaultValue", XPathUtil.formatElementOrAttribute(PMMLAttributes.INTSPARSEARRAY_DEFAULTVALUE));
		assertEquals("INT-SparseArray/INT-Entries", XPathUtil.formatElementOrAttribute(PMMLElements.INTSPARSEARRAY_ENTRIES));

		assertEquals("INT-SparseArray@n", XPathUtil.formatAttribute(PMMLAttributes.INTSPARSEARRAY_N, null));
		assertEquals("INT-SparseArray@n=0", XPathUtil.formatAttribute(PMMLAttributes.INTSPARSEARRAY_N, 0));
	}

	@Test
	public void formatTargetValueStat(){
		assertEquals("TargetValueStat/<ContinuousDistribution>", XPathUtil.formatElementOrAttribute(org.dmg.pmml.naive_bayes.PMMLElements.TARGETVALUESTAT_CONTINUOUSDISTRIBUTION));
	}
}