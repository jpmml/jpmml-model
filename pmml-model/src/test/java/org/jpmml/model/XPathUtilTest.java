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
	public void formatDataField() throws Exception {
		assertEquals("DataField", XPathUtil.formatElement(DataField.class));

		assertEquals("DataField@name", XPathUtil.formatElementOrAttribute(PMMLAttributes.DATAFIELD_NAME));
		assertEquals("DataField/Value", XPathUtil.formatElementOrAttribute(PMMLElements.DATAFIELD_VALUES));

		assertEquals("DataField@isCyclic", XPathUtil.formatAttribute(PMMLAttributes.DATAFIELD_CYCLIC, null));
		assertEquals("DataField@isCyclic=0", XPathUtil.formatAttribute(PMMLAttributes.DATAFIELD_CYCLIC, "0"));
	}

	@Test
	public void formatSparseArray(){
		assertEquals("INT-SparseArray", XPathUtil.formatElement(IntSparseArray.class));

		assertEquals("INT-SparseArray@defaultValue", XPathUtil.formatElementOrAttribute(PMMLAttributes.INTSPARSEARRAY_DEFAULTVALUE));
		assertEquals("INT-SparseArray/INT-Entries", XPathUtil.formatElementOrAttribute(PMMLElements.INTSPARSEARRAY_ENTRIES));

		assertEquals("INT-SparseArray@n", XPathUtil.formatAttribute(PMMLAttributes.INTSPARSEARRAY_N, null));
		assertEquals("INT-SparseArray@n=0", XPathUtil.formatAttribute(PMMLAttributes.INTSPARSEARRAY_N, 0));
	}
}