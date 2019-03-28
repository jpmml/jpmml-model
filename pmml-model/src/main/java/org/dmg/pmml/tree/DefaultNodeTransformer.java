/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public class DefaultNodeTransformer implements NodeTransformer {

	@Override
	public Node fromComplexNode(ComplexNode complexNode){

		if(complexNode.getRecordCount() != null){
			return complexNode;
		} // End if

		if(complexNode.hasExtensions() || (complexNode.getPartition() != null) || complexNode.hasScoreDistributions() || (complexNode.getEmbeddedModel() != null)){
			return complexNode;
		}

		Node node;

		if(complexNode.hasNodes()){
			node = new BranchNode(complexNode);
		} else

		{
			node = new LeafNode(complexNode);
		}

		return node;
	}

	@Override
	public ComplexNode toComplexNode(Node node){

		if(node instanceof ComplexNode){
			ComplexNode complexNode = (ComplexNode)node;

			return complexNode;
		}

		ComplexNode complexNode = new ComplexNode(node);

		return complexNode;
	}

	public static final DefaultNodeTransformer INSTANCE = new DefaultNodeTransformer();
}