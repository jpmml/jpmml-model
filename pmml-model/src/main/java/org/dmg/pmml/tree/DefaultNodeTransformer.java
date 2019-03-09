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
			node = new BranchNode()
				.setId(complexNode.getId())
				.setDefaultChild(complexNode.getDefaultChild());

			(node.getNodes()).addAll(complexNode.getNodes());
		} else

		{
			node = new LeafNode()
				.setId(complexNode.getId());
		}

		node
			.setScore(complexNode.getScore())
			.setPredicate(complexNode.getPredicate());

		return node;
	}

	@Override
	public ComplexNode toComplexNode(Node node){

		if(node instanceof ComplexNode){
			ComplexNode complexNode = (ComplexNode)node;

			return complexNode;
		}

		ComplexNode complexNode = new ComplexNode();

		complexNode.setId(node.getId());
		complexNode.setScore(node.getScore());
		complexNode.setRecordCount(node.getRecordCount());
		complexNode.setDefaultChild(node.getDefaultChild());

		if(node.hasExtensions()){
			(complexNode.getExtensions()).addAll(node.getExtensions());
		}

		complexNode.setPredicate(node.getPredicate());
		complexNode.setPartition(node.getPartition());

		if(node.hasScoreDistributions()){
			(complexNode.getScoreDistributions()).addAll(node.getScoreDistributions());
		} // End if

		if(node.hasNodes()){
			(complexNode.getNodes()).addAll(node.getNodes());
		}

		complexNode.setEmbeddedModel(node.getEmbeddedModel());

		return complexNode;
	}

	public static final DefaultNodeTransformer INSTANCE = new DefaultNodeTransformer();
}