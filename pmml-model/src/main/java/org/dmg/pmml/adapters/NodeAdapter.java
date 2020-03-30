/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.SimplifyingNodeTransformer;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.NodeTransformer;

public class NodeAdapter extends XmlAdapter<ComplexNode, Node> {

	@Override
	public Node unmarshal(ComplexNode value){
		NodeTransformer nodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		return nodeTransformer.fromComplexNode(value);
	}

	@Override
	public ComplexNode marshal(Node node){
		NodeTransformer nodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		return nodeTransformer.toComplexNode(node);
	}

	public static final ThreadLocal<NodeTransformer> NODE_TRANSFORMER_PROVIDER = ThreadLocal.withInitial(new Supplier<NodeTransformer>(){

		@Override
		public NodeTransformer get(){
			return SimplifyingNodeTransformer.INSTANCE;
		}
	});
}