/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.Node;
import org.dmg.pmml.tree.NodeTransformer;
import org.dmg.pmml.tree.SimplifyingNodeTransformer;

/**
 * @see NodeTransformer
 */
public class NodeAdapter extends XmlAdapter<ComplexNode, Node> {

	@Override
	public Node unmarshal(ComplexNode value){
		NodeTransformer nodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		if(nodeTransformer != null){
			return nodeTransformer.fromComplexNode(value);
		}

		return value;
	}

	@Override
	public ComplexNode marshal(Node node){
		NodeTransformer nodeTransformer = NodeAdapter.NODE_TRANSFORMER_PROVIDER.get();

		if(nodeTransformer != null){
			return nodeTransformer.toComplexNode(node);
		}

		return node.toComplexNode();
	}

	public static final ThreadLocal<NodeTransformer> NODE_TRANSFORMER_PROVIDER = new ThreadLocal<NodeTransformer>(){

		@Override
		public NodeTransformer initialValue(){
			return SimplifyingNodeTransformer.INSTANCE;
		}
	};
}