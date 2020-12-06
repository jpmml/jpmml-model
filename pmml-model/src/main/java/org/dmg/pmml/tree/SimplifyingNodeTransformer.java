/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

/**
 * <p>
 * A {@link Node} element converter between {@link ComplexNode} and {@link SimpleNode}.
 * </p>
 *
 * <p>
 * All conversions are information-preserving.
 * </p>
 */
public class SimplifyingNodeTransformer implements NodeTransformer {

	/**
	 * <p>
	 * Attempts to transform {@link ComplexNode} to the most memory-efficient {@link SimpleNode}.
	 * </p>
	 */
	@Override
	public Node fromComplexNode(ComplexNode complexNode){
		return simplify(complexNode);
	}

	@Override
	public ComplexNode toComplexNode(Node node){
		return node.toComplexNode();
	}

	public Node simplify(Node node){

		if(node.hasExtensions() || (node.getPartition() != null) || (node.getEmbeddedModel() != null)){
			return node;
		} // End if

		if(node.hasScoreDistributions()){
			return new ClassifierNode(node);
		}

		Number recordCount = node.getRecordCount();

		if(node.hasNodes()){

			if(recordCount != null){
				return new CountingBranchNode(node);
			} else

			{
				return new BranchNode(node);
			}
		} else

		{
			if(recordCount != null){
				return new CountingLeafNode(node);
			} else

			{
				return new LeafNode(node);
			}
		}
	}

	public static final SimplifyingNodeTransformer INSTANCE = new SimplifyingNodeTransformer();
}