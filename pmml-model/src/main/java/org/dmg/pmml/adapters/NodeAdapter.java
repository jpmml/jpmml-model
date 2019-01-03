/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.dmg.pmml.tree.BranchNode;
import org.dmg.pmml.tree.ComplexNode;
import org.dmg.pmml.tree.LeafNode;
import org.dmg.pmml.tree.Node;

public class NodeAdapter extends XmlAdapter<ComplexNode, Node> {

	@Override
	public Node unmarshal(ComplexNode value){

		if(value.getRecordCount() != null){
			return value;
		} // End if

		if(value.hasExtensions() || (value.getPartition() != null) || value.hasScoreDistributions() || (value.getEmbeddedModel() != null)){
			return value;
		}

		Node node;

		if(value.hasNodes()){
			node = new BranchNode()
				.setId(value.getId())
				.setDefaultChild(value.getDefaultChild());

			(node.getNodes()).addAll(value.getNodes());
		} else

		{
			node = new LeafNode()
				.setId(value.getId());
		}

		node
			.setScore(value.getScore())
			.setPredicate(value.getPredicate());

		return node;
	}

	@Override
	public ComplexNode marshal(Node node){

		if(node instanceof ComplexNode){
			ComplexNode complexNode = (ComplexNode)node;

			return complexNode;
		}

		ComplexNode value = new ComplexNode();

		value.setId(node.getId());
		value.setScore(node.getScore());
		value.setRecordCount(node.getRecordCount());
		value.setDefaultChild(node.getDefaultChild());

		if(node.hasExtensions()){
			(value.getExtensions()).addAll(node.getExtensions());
		}

		value.setPredicate(node.getPredicate());
		value.setPartition(node.getPartition());

		if(node.hasScoreDistributions()){
			(value.getScoreDistributions()).addAll(node.getScoreDistributions());
		} // End if

		if(node.hasNodes()){
			(value.getNodes()).addAll(node.getNodes());
		}

		value.setEmbeddedModel(node.getEmbeddedModel());

		return value;
	}
}