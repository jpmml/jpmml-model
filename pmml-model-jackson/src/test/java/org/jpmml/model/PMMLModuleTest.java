/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PMMLModuleTest {

	@Test
	public void jsonClone() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new PMMLModule());

		DataField dataField = new DataField(FieldName.create("x"), OpType.CATEGORICAL, DataType.BOOLEAN);

		DataDictionary dataDictionary = new DataDictionary()
			.addDataFields(dataField);

		MiningField miningField = new MiningField(FieldName.create("x"));

		MiningSchema miningSchema = new MiningSchema()
			.addMiningFields(miningField);

		SimplePredicate simplePredicate = new SimplePredicate(FieldName.create("x"), SimplePredicate.Operator.IS_NOT_MISSING);

		Node node = new Node()
			.setPredicate(simplePredicate);

		TreeModel treeModel = new TreeModel()
			.setMiningSchema(miningSchema)
			.setNode(node);

		PMML pmml = new PMML()
			.setDataDictionary(dataDictionary)
			.addModels(treeModel);

		String json = mapper.writeValueAsString(pmml);

		PMML jsonPmml = mapper.readValue(json, PMML.class);

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

		Node jsonNode = jsonTreeModel.getNode();

		SimplePredicate jsonSimplePredicate = (SimplePredicate)jsonNode.getPredicate();

		assertEquals(simplePredicate.getField(), jsonSimplePredicate.getField());
		assertEquals(simplePredicate.getOperator(), jsonSimplePredicate.getOperator());
	}
}