/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.Deque;
import java.util.Iterator;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Header;
import org.dmg.pmml.MiningFunctionType;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Node;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimpleSetPredicate;
import org.dmg.pmml.TreeModel;
import org.dmg.pmml.True;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.visitors.AbstractVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VisitorTest {

	@Test
	public void visit(){
		PMML pmml = new PMML("4.2", new Header(), new DataDictionary());

		Node root = new Node()
			.withPredicate(new True());

		final
		Node leftChild = new Node()
			.withPredicate(new SimplePredicate());

		final
		Node rightChild = new Node()
			.withPredicate(new SimpleSetPredicate());

		root = root.withNodes(leftChild, rightChild);

		TreeModel treeModel = new TreeModel(MiningFunctionType.CLASSIFICATION, new MiningSchema(), root);

		pmml = pmml.withModels(treeModel);

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
				checkParents(PMML.class, TreeModel.class, Node.class);

				return super.visit(_true);
			}

			@Override
			public VisitorAction visit(SimplePredicate simplePredicate){
				checkParents(PMML.class, TreeModel.class, Node.class, Node.class);

				return super.visit(simplePredicate);
			}

			@Override
			public VisitorAction visit(SimpleSetPredicate simpleSetPredicate){
				checkParents(PMML.class, TreeModel.class, Node.class, Node.class);

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