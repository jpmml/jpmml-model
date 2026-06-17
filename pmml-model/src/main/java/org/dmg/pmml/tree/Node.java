/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.List;

import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.EmbeddedModel;
import org.dmg.pmml.Entity;
import org.dmg.pmml.Extension;
import org.dmg.pmml.HasPredicate;
import org.dmg.pmml.HasRecordCount;
import org.dmg.pmml.HasScore;
import org.dmg.pmml.HasScoreDistributions;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Partition;
import org.dmg.pmml.Payload;
import org.dmg.pmml.ScoreDistribution;
import org.dmg.pmml.adapters.NodeAdapter;

@XmlTransient
@XmlJavaTypeAdapter (
	value = NodeAdapter.class
)
abstract
public class Node extends Entity<Object> implements HasPredicate<Node>, HasRecordCount<Node>, HasScore<Node>, HasScoreDistributions<Node> {

	public ComplexNode toComplexNode(){
		return new ComplexNode(this);
	}

	@Override
	public Object getId(){
		return null;
	}

	@Override
	public Node setId(Object id){
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getScore(){
		return null;
	}

	@Override
	public Node setScore(Object score){
		throw new UnsupportedOperationException();
	}

	@Override
	public Number getRecordCount(){
		return null;
	}

	@Override
	public Node setRecordCount(Number recordCount){
		throw new UnsupportedOperationException();
	}

	public Object requireDefaultChild(){
		throw new UnsupportedOperationException();
	}

	public Object getDefaultChild(){
		return null;
	}

	public Node setDefaultChild(Object defaultChild){
		throw new UnsupportedOperationException();
	}

	public boolean hasExtensions(){
		return false;
	}

	public List<Extension> getExtensions(){
		throw new UnsupportedOperationException();
	}

	public Node addExtensions(Extension... extensions){
		PMMLObject.addElements(getExtensions(), extensions);

		return this;
	}

	public Partition getPartition(){
		return null;
	}

	public Node setPartition(Partition partition){
		throw new UnsupportedOperationException();
	}

	public boolean hasPayloads(){
		return false;
	}

	public List<Payload> getPayloads(){
		throw new UnsupportedOperationException();
	}

	public Node addPayloads(Payload... payloads){
		PMMLObject.addElements(getPayloads(), payloads);

		return this;
	}

	@Override
	public boolean hasScoreDistributions(){
		return hasPayloads();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List<ScoreDistribution> getScoreDistributions(){
		return (List)getPayloads();
	}

	@Override
	public Node addScoreDistributions(ScoreDistribution... scoreDistributions){
		return addPayloads(scoreDistributions);
	}

	public boolean hasNodes(){
		return false;
	}

	public List<Node> getNodes(){
		throw new UnsupportedOperationException();
	}

	public Node addNodes(Node first){
		List<Node> nodes = getNodes();

		nodes.add(first);

		return this;
	}

	public Node addNodes(Node first, Node second){
		List<Node> nodes = getNodes();

		nodes.add(first);
		nodes.add(second);

		return this;
	}

	public Node addNodes(Node... nodes){
		PMMLObject.addElements(getNodes(), nodes);

		return this;
	}

	public EmbeddedModel getEmbeddedModel(){
		return null;
	}

	public Node setEmbeddedModel(EmbeddedModel embeddedModel){
		throw new UnsupportedOperationException();
	}
}