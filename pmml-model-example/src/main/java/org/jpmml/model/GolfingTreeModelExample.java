/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.ArrayList;
import java.util.List;

import org.dmg.pmml.CompoundPredicate;
import org.dmg.pmml.CompoundPredicate.BooleanOperator;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.Header;
import org.dmg.pmml.MiningField;
import org.dmg.pmml.MiningFunction;
import org.dmg.pmml.MiningSchema;
import org.dmg.pmml.OpType;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Predicate;
import org.dmg.pmml.SimplePredicate;
import org.dmg.pmml.SimplePredicate.Operator;
import org.dmg.pmml.True;
import org.dmg.pmml.Value;
import org.dmg.pmml.Version;
import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.TreeModel;

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
			.setCopyright("www.dmg.org")
			.setDescription("A very small binary tree model to show structure.");

		DataDictionary dataDictionary = new DataDictionary()
			.addDataFields(
				new DataField(temperature, OpType.CONTINUOUS, DataType.DOUBLE),
				new DataField(humidity, OpType.CONTINUOUS, DataType.DOUBLE),
				new DataField(windy, OpType.CATEGORICAL, DataType.STRING)
					.addValues(createValues("true", "false")),
				new DataField(outlook, OpType.CATEGORICAL, DataType.STRING)
					.addValues(createValues("sunny", "overcast", "rain")),
				new DataField(whatIdo, OpType.CATEGORICAL, DataType.STRING)
					.addValues(createValues("will play", "may play", "no play"))
			);

		dataDictionary.setNumberOfFields((dataDictionary.getDataFields()).size());

		MiningSchema miningSchema = new MiningSchema()
			.addMiningFields(
				new MiningField(temperature),
				new MiningField(humidity),
				new MiningField(windy),
				new MiningField(outlook),
				new MiningField(whatIdo)
					.setUsageType(MiningField.UsageType.TARGET)
			);

		Node root = new BranchNode("will play", new True());

		// Upper half of the tree
		root.addNodes(
			new BranchNode("will play", new SimplePredicate(outlook, Operator.EQUAL, "sunny"))
				.addNodes(
					new BranchNode("will play",
						createCompoundPredicate(BooleanOperator.AND,
							new SimplePredicate(temperature, Operator.LESS_THAN, "90"),
							new SimplePredicate(temperature, Operator.GREATER_THAN, "50"))
						)
						.addNodes(
							new LeafNode("will play", new SimplePredicate(humidity, Operator.LESS_THAN, "80")),
							new LeafNode("no play", new SimplePredicate(humidity, Operator.GREATER_OR_EQUAL, "80"))
						),
					new LeafNode("no play",
						createCompoundPredicate(BooleanOperator.OR,
							new SimplePredicate(temperature, Operator.GREATER_OR_EQUAL, "90"),
							new SimplePredicate(temperature, Operator.LESS_OR_EQUAL, "50"))
						)
				)
		);

		// Lower half of the tree
		root.addNodes(
			new BranchNode("may play",
				createCompoundPredicate(BooleanOperator.OR,
					new SimplePredicate(outlook, Operator.EQUAL, "overcast"),
					new SimplePredicate(outlook, Operator.EQUAL, "rain"))
				)
				.addNodes(
					new LeafNode("may play",
						createCompoundPredicate(BooleanOperator.AND,
							new SimplePredicate(temperature, Operator.GREATER_THAN, "60"),
							new SimplePredicate(temperature, Operator.LESS_THAN, "100"),
							new SimplePredicate(outlook, Operator.EQUAL, "overcast"),
							new SimplePredicate(humidity, Operator.LESS_THAN, "70"),
							new SimplePredicate(windy, Operator.EQUAL, "false"))
						),
					new LeafNode("no play",
						createCompoundPredicate(BooleanOperator.AND,
							new SimplePredicate(outlook, Operator.EQUAL, "rain"),
							new SimplePredicate(humidity, Operator.LESS_THAN, "70"))
						)
				)
		);

		TreeModel treeModel = new TreeModel(MiningFunction.CLASSIFICATION, miningSchema, root)
			.setModelName("golfing");

		PMML pmml = new PMML(Version.PMML_4_3.getVersion(), header, dataDictionary)
			.addModels(treeModel);

		return pmml;
	}

	static
	private Value[] createValues(String... values){
		List<Value> result = new ArrayList<>();

		for(String value : values){
			result.add(new Value(value));
		}

		return result.toArray(new Value[result.size()]);
	}

	static
	private CompoundPredicate createCompoundPredicate(CompoundPredicate.BooleanOperator booleanOperator, Predicate... predicates){
		return new CompoundPredicate(booleanOperator, null)
			.addPredicates(predicates);
	}
}
