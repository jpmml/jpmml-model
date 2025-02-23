/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.jpmml.model.resources;

import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.PMML;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.adapters.NodeAdapter;
import org.dmg.pmml.tree.ClassifierNode;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.CountingBranchNode;
import org.dmg.pmml.tree.CountingLeafNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.NodeTransformer;
import org.jpmml.model.JAXBSerializer;
import org.jpmml.model.visitors.AbstractVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

abstract
public class NodePolymorphismTest {

	private NodePolymorphismTest(){
	}

	static
	public PMML load(NodeTransformer nodeTransformer) throws Exception {
		JAXBSerializer serializer = new JAXBSerializer();

		return load(serializer, nodeTransformer);
	}

	static
	public PMML load(JAXBSerializer serializer, NodeTransformer nodeTransformer) throws Exception {
		NodeTransformer defaultNodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		try {
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(nodeTransformer);

			return ResourceUtil.unmarshal(serializer, NodePolymorphismTest.class);
		} finally {
			NodeAdapter.NODE_TRANSFORMER_PROVIDER.set(defaultNodeTransformer);
		}
	}

	static
	public void checkComplex(PMML pmml){
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
	}

	static
	public void checkSimplified(PMML pmml){
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
}