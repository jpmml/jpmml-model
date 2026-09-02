/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.List;

import org.dmg.pmml.ComplexScoreDistribution;
import org.dmg.pmml.Extension;
import org.dmg.pmml.False;
import org.dmg.pmml.Payload;
import org.dmg.pmml.Score;
import org.dmg.pmml.ScoreDistribution;
import org.dmg.pmml.True;
import org.dmg.pmml.adapters.NumberUtil;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.SerializationUtil;
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
	public void jaxbCloneNodes() throws Exception {
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

		TreeModel jaxbTreeModel = SerializationUtil.clone(serializer, treeModel);

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

	@Test
	public void jaxbClonePayloads() throws Exception {
		Serializer serializer = new JAXBSerializer();

		Node node = new ComplexNode(null, True.INSTANCE)
			.addPayloads(new Score("y1", 0.5d), new ComplexScoreDistribution("a", 1), new Score("y2", 1.5d));

		Node jaxbNode = SerializationUtil.clone(serializer, node);

		assertEquals(node.getClass(), jaxbNode.getClass());

		List<Payload> payloads = jaxbNode.getPayloads();

		assertEquals(3, payloads.size());

		Score firstScore = (Score)payloads.get(0);
		ScoreDistribution scoreDistribution = (ScoreDistribution)payloads.get(1);
		Score secondScore = (Score)payloads.get(2);

		assertEquals("y1", firstScore.getTargetField());
		assertEquals(NumberUtil.printNumber(0.5d), firstScore.getValue());

		assertEquals("a", scoreDistribution.getValue());

		assertEquals("y2", secondScore.getTargetField());
		assertEquals(NumberUtil.printNumber(1.5d), secondScore.getValue());
	}
}