/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.tree.ClassifierNode;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.CountingBranchNode;
import org.dmg.pmml.tree.CountingLeafNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.NodeTransformer;
import org.dmg.pmml.tree.SimplifyingNodeTransformer;
import org.jpmml.model.PMMLUtil;
import org.jpmml.model.ResourceUtil;
import org.jpmml.model.visitors.AbstractVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NodeAdapterTest {

	@Test
	public void loadComplex() throws Exception {
		NodeTransformer beforeNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		assertTrue(beforeNodeTransformer instanceof SimplifyingNodeTransformer);

		PMML pmml = load(null);

		Set<Node> visitedNodes = new HashSet<>();

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Node node){
				visitedNodes.add(node);

				assertTrue(node instanceof ComplexNode);

				return super.visit(node);
			}
		};
		visitor.applyTo(pmml);

		assertEquals(3, visitedNodes.size());

		NodeTransformer afterNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		assertSame(beforeNodeTransformer, afterNodeTransformer);
	}

	@Test
	public void loadSimplified() throws Exception {
		PMML pmml = load(SimplifyingNodeTransformer.INSTANCE);

		Set<Node> visitedNodes = new HashSet<>();

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Node node){
				Object id = node.getId();

				visitedNodes.add(node);

				if(("parent").equals(id)){
					assertTrue(node instanceof CountingBranchNode);
				} else

				if(("false child").equals(id)){
					assertTrue(node instanceof LeafNode);
					assertFalse(node instanceof CountingLeafNode);
				} else

				if(("true child").equals(id)){
					assertTrue(node instanceof ClassifierNode);
				} else

				{
					fail();
				}

				return super.visit(node);
			}
		};
		visitor.applyTo(pmml);

		assertEquals(3, visitedNodes.size());
	}

	static
	private PMML load(NodeTransformer nodeTransformer) throws Exception {
		NodeTransformer defaultNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		try(InputStream is = ResourceUtil.getStream(NodeAdapterTest.class)){
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(nodeTransformer);

			return PMMLUtil.unmarshal(is);
		} finally {
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(defaultNodeTransformer);
		}
	}
}