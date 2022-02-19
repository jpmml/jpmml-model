/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.List;

import org.dmg.pmml.Extension;
import org.dmg.pmml.False;
import org.dmg.pmml.True;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.UnsupportedElementException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeTest {

	@Test
	public void jaxbClone() throws Exception {
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

		TreeModel jaxbTreeModel = JAXBUtil.clone(treeModel);

		Node jaxbNode1 = jaxbTreeModel.getNode();

		assertEquals(node1.getClass(), jaxbNode1.getClass());
		assertEquals(1, node1.getId());
		assertEquals("1", jaxbNode1.getId());
		assertEquals(node2b, node1.requireDefaultChild());
		assertEquals("2b", jaxbNode1.requireDefaultChild());

		assertSame(True.INSTANCE, node1.requirePredicate(True.class));

		try {
			node1.requirePredicate(False.class);

			fail();
		} catch(UnsupportedElementException uee){
			// Ignored
		}

		assertNotSame(True.INSTANCE, jaxbNode1.requirePredicate(True.class));

		try {
			jaxbNode1.requirePredicate(False.class);
		} catch(UnsupportedElementException uee){
			// Ignored
		}

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
}