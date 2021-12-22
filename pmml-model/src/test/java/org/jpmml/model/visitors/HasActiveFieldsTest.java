/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.DataType;
import org.dmg.pmml.DerivedField;
import org.dmg.pmml.FieldRef;
import org.dmg.pmml.ForwardingModel;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.Model;
import org.dmg.pmml.OpType;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HasActiveFieldsTest {

	@Test
	public void find(){
		MiningSchema miningSchema = new MiningSchema()
			.addMiningFields(new MiningField("x"));

		DerivedField derivedField = new DerivedField("float(x)", OpType.CONTINUOUS, DataType.FLOAT, new FieldRef("x"));

		LocalTransformations localTranformations = new LocalTransformations()
			.addDerivedFields(derivedField);

		Node root = new LeafNode(1d, new SimplePredicate(derivedField, SimplePredicate.Operator.IS_NOT_MISSING, null));

		Model model = new TreeModel(MiningFunction.REGRESSION, miningSchema, root)
			.setLocalTransformations(localTranformations);

		checkFields(Arrays.asList("float(x)", "x"), model);

		model = new CustomModel(model){

			@Override
			public Set<String> getActiveFields(){
				return Collections.singleton("x");
			}

			@Override
			public VisitorAction accept(Visitor visitor){

				if(visitor instanceof ActiveFieldFinder){
					VisitorAction status = visitor.visit(this);

					return VisitorAction.SKIP;
				}

				return super.accept(visitor);
			}
		};

		checkFields(Arrays.asList("x"), model);
	}

	static
	private void checkFields(Collection<String> names, Model model){
		assertEquals(new HashSet<>(names), ActiveFieldFinder.getFieldNames(model));
	}

	static
	abstract
	private class CustomModel extends ForwardingModel implements HasActiveFields {

		private CustomModel(Model model){
			super(model);
		}
	}
}