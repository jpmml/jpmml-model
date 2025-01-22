/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.io.InputStream;
import java.util.List;

import org.dmg.pmml.Extension;
import org.dmg.pmml.False;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.True;
import org.jpmml.model.DirectByteArrayOutputStream;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.Serializer;
import org.jpmml.model.UnsupportedElementException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NodeTest {

	@Test
	public void jaxbClone() throws Exception {
		Serializer serializer = new JAXBSerializer();

		Node node1 = new BranchNode(null, True.INSTANCE)
			.setId(1);

		List<Node> nodes = node1.getNodes();

		Node node2a = new ComplexNode(null, False.INSTANCE)
			.setId("2a")
			.addExtensions(new Extension());

		nodes.add(node2a);

		Node node2b = new LeafNode(null, True.INSTANCE)
			.setId("2b");

		nodes.add(node2b);

		node1
			.setDefaultChild(node2b);

		TreeModel treeModel = new TreeModel()
			.setNode(node1);

		TreeModel jaxbTreeModel = clone(serializer, treeModel);

		Node jaxbNode1 = jaxbTreeModel.getNode();

		assertEquals(node1.getClass(), jaxbNode1.getClass());
		assertEquals(1, node1.getId());
		assertEquals("1", jaxbNode1.getId());
		assertEquals(node2b, node1.requireDefaultChild());
		assertEquals("2b", jaxbNode1.requireDefaultChild());

		assertSame(True.INSTANCE, node1.requirePredicate(True.class));
		assertThrows(UnsupportedElementException.class, () -> node1.requirePredicate(False.class));

		assertNotSame(True.INSTANCE, jaxbNode1.requirePredicate(True.class));
		assertThrows(UnsupportedElementException.class, () -> jaxbNode1.requirePredicate(False.class));

		List<Node> jaxbNodes = jaxbNode1.getNodes();

		assertEquals(2, jaxbNodes.size());

		Node jaxbNode2a = jaxbNodes.get(0);

		assertEquals(node2a.getClass(), jaxbNode2a.getClass());
		assertEquals(node2a.getId(), jaxbNode2a.getId());

		assertTrue(jaxbNode2a.hasExtensions());

		Node jaxbNode2b = jaxbNodes.get(1);

		assertEquals(node2b.getClass(), jaxbNode2b.getClass());
		assertEquals(node2b.getId(), jaxbNode2b.getId());
	}

	static
	private <E extends PMMLObject> E clone(Serializer serializer, E object) throws Exception {
		DirectByteArrayOutputStream buffer = new DirectByteArrayOutputStream(10 * 1024);

		serializer.serialize(object, buffer);

		try(InputStream is = buffer.getInputStream()){
			@SuppressWarnings("unchecked")
			E clonedObject = (E)serializer.deserialize(is);

			return clonedObject;
		}
	}
}