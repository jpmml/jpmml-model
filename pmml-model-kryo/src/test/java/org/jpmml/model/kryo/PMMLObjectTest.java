/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.kryo;

import java.util.List;

import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.False;
import org.dmg.pmml.OpType;
import org.dmg.pmml.PMML;
import org.dmg.pmml.True;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.jpmml.model.Serializer;
import org.jpmml.model.resources.CellPolymorphismTest;
import org.jpmml.model.resources.ExtensionTest;
import org.jpmml.model.resources.ResourceUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PMMLObjectTest extends KryoSerializerTest {

	@Test
	public void dataField() throws Exception {
		Serializer serializer = new KryoSerializer(super.kryo);

		DataField dataField = new DataField("x", OpType.CONTINUOUS, DataType.DOUBLE);

		DataField clonedDataField = checkedClone(serializer, dataField);

		// XXX
		assertEquals(dataField.getName(), clonedDataField.getName());
		assertNotSame(dataField.getName(), clonedDataField.getName());
	}

	@Test
	public void node() throws Exception {
		Serializer serializer = new KryoSerializer(super.kryo);

		Node leftChild = new LeafNode()
			.setPredicate(True.INSTANCE);

		Node rightChild = new LeafNode()
			.setPredicate(False.INSTANCE);

		Node parent = new BranchNode()
			.setPredicate(True.INSTANCE)
			.setDefaultChild(leftChild)
			.addNodes(leftChild, rightChild);

		Node clonedParent = checkedClone(serializer, parent);

		List<Node> clonedChildren = clonedParent.getNodes();

		assertNotSame(parent, clonedParent);

		Node clonedLeftChild = clonedChildren.get(0);
		Node clonedRightChild = clonedChildren.get(1);

		assertSame(clonedParent.requireDefaultChild(), clonedLeftChild);

		assertNotSame(leftChild, clonedLeftChild);
		assertNotSame(rightChild, clonedRightChild);

		assertNotSame(parent.requirePredicate(), clonedParent.requirePredicate());
		assertSame(clonedParent.requirePredicate(), clonedLeftChild.requirePredicate());
	}

	@Test
	public void inlineTable() throws Exception {
		Serializer serializer = new KryoSerializer(super.kryo);

		PMML pmml = ResourceUtil.unmarshal(CellPolymorphismTest.class);

		checkedClone(serializer, pmml);
	}

	@Test
	public void mixedContent() throws Exception {
		Serializer serializer = new KryoSerializer(super.kryo);

		PMML pmml = ResourceUtil.unmarshal(ExtensionTest.class);

		checkedClone(serializer, pmml);
	}
}