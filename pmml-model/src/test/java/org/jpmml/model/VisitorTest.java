/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Deque;
import java.util.Iterator;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.True;
import org.dmg.pmml.Version;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.jpmml.model.visitors.AbstractVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VisitorTest {

	@Test
	public void visit(){
		Node leftChild = new ComplexNode()
			.setPredicate(new SimplePredicate());

		Node rightChild = new ComplexNode()
			.setPredicate(new SimpleSetPredicate());

		Node root = new ComplexNode()
			.setPredicate(new True())
			.addNodes(leftChild, rightChild);

		TreeModel treeModel = new TreeModel(MiningFunction.CLASSIFICATION, new MiningSchema(), root)
			.setSplitCharacteristic(TreeModel.SplitCharacteristic.BINARY_SPLIT);

		PMML pmml = new PMML(Version.PMML_4_3.getVersion(), new Header(), new DataDictionary())
			.addModels(treeModel);

		Visitor skipVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(TreeModel treeModel){
				return VisitorAction.SKIP;
			}

			@Override
			public VisitorAction visit(Node node){
				throw new AssertionError();
			}
		};

		pmml.accept(skipVisitor);

		Visitor skipLeftVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Node node){

				if(leftChild.equals(node)){
					return VisitorAction.SKIP;
				}

				return super.visit(node);
			}

			@Override
			public VisitorAction visit(SimplePredicate simplePredicate){
				throw new AssertionError();
			}
		};

		pmml.accept(skipLeftVisitor);

		Visitor terminateVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(TreeModel treeModel){
				return VisitorAction.TERMINATE;
			}

			@Override
			public VisitorAction visit(Node node){
				throw new AssertionError();
			}
		};

		pmml.accept(terminateVisitor);

		Visitor terminateLeftVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Node node){

				if(leftChild.equals(node)){
					return VisitorAction.TERMINATE;
				}

				return super.visit(node);
			}

			@Override
			public VisitorAction visit(SimplePredicate simplePredicate){
				throw new AssertionError();
			}

			@Override
			public VisitorAction visit(SimpleSetPredicate simpleSetPredicate){
				throw new AssertionError();
			}
		};

		pmml.accept(terminateLeftVisitor);

		Visitor parentVisitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(PMML pmml){
				checkParents();

				return super.visit(pmml);
			}

			@Override
			public VisitorAction visit(TreeModel treeModel){
				checkParents(PMML.class);

				return super.visit(treeModel);
			}

			@Override
			public VisitorAction visit(True _true){
				checkParents(PMML.class, TreeModel.class, ComplexNode.class);

				return super.visit(_true);
			}

			@Override
			public VisitorAction visit(SimplePredicate simplePredicate){
				checkParents(PMML.class, TreeModel.class, ComplexNode.class, ComplexNode.class);

				return super.visit(simplePredicate);
			}

			@Override
			public VisitorAction visit(SimpleSetPredicate simpleSetPredicate){
				checkParents(PMML.class, TreeModel.class, ComplexNode.class, ComplexNode.class);

				return super.visit(simpleSetPredicate);
			}

			private void checkParents(Class<?>... clazzes){
				Deque<PMMLObject> parents = getParents();

				assertEquals(clazzes.length, parents.size());

				int i = (clazzes.length - 1);

				// Iterates from the nearest parent to the farthest parent
				for(Iterator<PMMLObject> it = parents.iterator(); it.hasNext(); ){
					PMMLObject parent = it.next();

					assertEquals(clazzes[i], parent.getClass());

					i--;
				}
			}
		};

		pmml.accept(parentVisitor);
	}
}
