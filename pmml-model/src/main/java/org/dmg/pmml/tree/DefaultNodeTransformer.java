/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

public class DefaultNodeTransformer implements NodeTransformer {

	@Override
	public Node fromComplexNode(ComplexNode complexNode){

		if(complexNode.hasExtensions() || (complexNode.getPartition() != null) || (complexNode.getEmbeddedModel() != null)){
			return complexNode;
		} // End if

		if(complexNode.hasScoreDistributions()){
			return new ClassifierNode(complexNode);
		}

		Double recordCount = complexNode.getRecordCount();

		if(complexNode.hasNodes()){

			if(recordCount != null){
				return new CountingBranchNode(complexNode);
			} else

			{
				return new BranchNode(complexNode);
			}
		} else

		{
			if(recordCount != null){
				return new CountingLeafNode(complexNode);
			} else

			{
				return new LeafNode(complexNode);
			}
		}
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