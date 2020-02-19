/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.InputStream;
import java.util.List;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.OpType;
import org.dmg.pmml.PMML;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.model.jackson.JacksonUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ObjectMapperTest {

	@Test
	public void jsonClone() throws Exception {
		DataField dataField = new DataField(FieldName.create("x"), OpType.CATEGORICAL, DataType.BOOLEAN);

		DataDictionary dataDictionary = new DataDictionary()
			.addDataFields(dataField);

		MiningField miningField = new MiningField(FieldName.create("x"));

		MiningSchema miningSchema = new MiningSchema()
			.addMiningFields(miningField);

		assertSame(dataField.getName(), miningField.getName());

		SimplePredicate simplePredicate = new SimplePredicate(FieldName.create("x"), SimplePredicate.Operator.IS_NOT_MISSING, null);

		Node node = new ComplexNode(simplePredicate);

		TreeModel treeModel = new TreeModel()
			.setMiningSchema(miningSchema)
			.setNode(node);

		PMML pmml = new PMML()
			.setDataDictionary(dataDictionary)
			.addModels(treeModel);

		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(1024);

		JacksonUtil.writePMML(pmml, buffer);

		PMML jsonPmml;

		try(InputStream is = buffer.getInputStream()){
			jsonPmml = JacksonUtil.readPMML(is);
		}

		DataDictionary jsonDataDictionary = jsonPmml.getDataDictionary();

		List<DataField> jsonDataFields = jsonDataDictionary.getDataFields();

		assertEquals(1, jsonDataFields.size());

		DataField jsonDataField = jsonDataFields.get(0);

		assertEquals(dataField.getName(), jsonDataField.getName());
		assertEquals(dataField.getOpType(), jsonDataField.getOpType());
		assertEquals(dataField.getDataType(), jsonDataField.getDataType());

		List<Model> jsonModels = jsonPmml.getModels();

		assertEquals(1, jsonModels.size());

		TreeModel jsonTreeModel = (TreeModel)jsonModels.get(0);

		MiningSchema jsonMiningSchema = jsonTreeModel.getMiningSchema();

		List<MiningField> jsonMiningFields = jsonMiningSchema.getMiningFields();

		assertEquals(1, jsonMiningFields.size());

		MiningField jsonMiningField = jsonMiningFields.get(0);

		assertEquals(miningField.getName(), jsonMiningField.getName());
		assertEquals(miningField.getUsageType(), jsonMiningField.getUsageType());

		assertSame(jsonDataField.getName(), jsonMiningField.getName());

		Node jsonNode = jsonTreeModel.getNode();

		SimplePredicate jsonSimplePredicate = (SimplePredicate)jsonNode.getPredicate();

		assertEquals(simplePredicate.getField(), jsonSimplePredicate.getField());
		assertEquals(simplePredicate.getOperator(), jsonSimplePredicate.getOperator());

		assertSame(jsonDataField.getName(), jsonSimplePredicate.getField());
		assertSame(jsonMiningField.getName(), jsonSimplePredicate.getField());
	}
}