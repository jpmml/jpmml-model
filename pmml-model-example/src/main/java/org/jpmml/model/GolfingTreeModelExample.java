/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.*;

import org.dmg.pmml.*;
import org.dmg.pmml.CompoundPredicate.BooleanOperator;
import org.dmg.pmml.SimplePredicate.Operator;

public class GolfingTreeModelExample extends ProductionExample {

	static
	public void main(String... args) throws Exception {
		execute(GolfingTreeModelExample.class, args);
	}

	@Override
	public PMML produce(){
		FieldName temperature = FieldName.create("temperature");
		FieldName humidity = FieldName.create("humidity");
		FieldName windy = FieldName.create("windy");
		FieldName outlook = FieldName.create("outlook");
		FieldName whatIdo = FieldName.create("whatIDo");

		Header header = new Header()
			.withCopyright("www.dmg.org")
			.withDescription("A very small binary tree model to show structure.");

		DataDictionary dataDictionary = new DataDictionary()
			.withDataFields(
				new DataField(temperature, OpType.CONTINUOUS, DataType.DOUBLE),
				new DataField(humidity, OpType.CONTINUOUS, DataType.DOUBLE),
				new DataField(windy, OpType.CATEGORICAL, DataType.STRING)
					.withValues(createValues("true", "false")),
				new DataField(outlook, OpType.CATEGORICAL, DataType.STRING)
					.withValues(createValues("sunny", "overcast", "rain")),
				new DataField(whatIdo, OpType.CATEGORICAL, DataType.STRING)
					.withValues(createValues("will play", "may play", "no play"))
			);
		dataDictionary.withNumberOfFields((dataDictionary.getDataFields()).size());

		PMML pmml = new PMML(header, dataDictionary, "4.2");

		MiningSchema miningSchema = new MiningSchema()
			.withMiningFields(
				new MiningField(temperature),
				new MiningField(humidity),
				new MiningField(windy),
				new MiningField(outlook),
				new MiningField(whatIdo)
					.withUsageType(FieldUsageType.TARGET)
			);

		Node root = createNode("will play", new True());

		// Upper half of the tree
		root.withNodes(
			createNode("will play", createSimplePredicate(outlook, Operator.EQUAL, "sunny"))
				.withNodes(
					createNode("will play",
						createCompoundPredicate(BooleanOperator.AND,
							createSimplePredicate(temperature, Operator.LESS_THAN, "90"),
							createSimplePredicate(temperature, Operator.GREATER_THAN, "50"))
						)
						.withNodes(
							createNode("will play", createSimplePredicate(humidity, Operator.LESS_THAN, "80")),
							createNode("no play", createSimplePredicate(humidity, Operator.GREATER_OR_EQUAL, "80"))
						),
					createNode("no play",
						createCompoundPredicate(BooleanOperator.OR,
							createSimplePredicate(temperature, Operator.GREATER_OR_EQUAL, "90"),
							createSimplePredicate(temperature, Operator.LESS_OR_EQUAL, "50"))
						)
				)
		);

		// Lower half of the tree
		root.withNodes(
			createNode("may play",
				createCompoundPredicate(BooleanOperator.OR,
					createSimplePredicate(outlook, Operator.EQUAL, "overcast"),
					createSimplePredicate(outlook, Operator.EQUAL, "rain"))
				)
				.withNodes(
					createNode("may play",
						createCompoundPredicate(BooleanOperator.AND,
							createSimplePredicate(temperature, Operator.GREATER_THAN, "60"),
							createSimplePredicate(temperature, Operator.LESS_THAN, "100"),
							createSimplePredicate(outlook, Operator.EQUAL, "overcast"),
							createSimplePredicate(humidity, Operator.LESS_THAN, "70"),
							createSimplePredicate(windy, Operator.EQUAL, "false"))
						),
					createNode("no play",
						createCompoundPredicate(BooleanOperator.AND,
							createSimplePredicate(outlook, Operator.EQUAL, "rain"),
							createSimplePredicate(humidity, Operator.LESS_THAN, "70"))
						)
				)
		);

		TreeModel treeModel = new TreeModel(miningSchema, root, MiningFunctionType.CLASSIFICATION)
			.withModelName("golfing");

		pmml.withModels(treeModel);

		return pmml;
	}

	static
	private List<Value> createValues(String... values){
		List<Value> result = new ArrayList<Value>();

		for(String value : values){
			result.add(new Value(value));
		}

		return result;
	}

	static
	private Node createNode(String score, Predicate predicate){
		return new Node()
			.withScore(score)
			.withPredicate(predicate);
	}

	static
	private SimplePredicate createSimplePredicate(FieldName name, SimplePredicate.Operator operator, String value){
		return new SimplePredicate(name, operator)
			.withValue(value);
	}

	static
	private CompoundPredicate createCompoundPredicate(CompoundPredicate.BooleanOperator booleanOperator, Predicate... predicates){
		return new CompoundPredicate(booleanOperator)
			.withPredicates(predicates);
	}
}