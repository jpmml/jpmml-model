/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.tree.Node;

/**
 * <p>
 * A Visitor that determines paths from the root Node element of the tree model to all its leaf Node elements.
 * </p>
 */
public class TreePathFinder extends AbstractVisitor implements Resettable {

	private Map<Node, List<Node>> paths = new HashMap<>();


	@Override
	public void reset(){
		this.paths.clear();
	}

	@Override
	public VisitorAction visit(Node node){

		if(!node.hasNodes()){
			process(node);
		}

		return super.visit(node);
	}

	private void process(Node node){
		List<Node> path = new ArrayList<>();
		path.add(node);

		Deque<PMMLObject> parents = getParents();
		for(PMMLObject parent : parents){

			if(!(parent instanceof Node)){
				break;
			}

			path.add((Node)parent);
		}

		Collections.reverse(path);

		this.paths.put(node, path);
	}

	/**
	 * @return A map of all paths.
	 * Map keys are leaf Node elements.
	 * Map values are paths leading from the root Node element to the specified leaf Node element (inclusive).
	 */
	public Map<Node, List<Node>> getPaths(){
		return Collections.unmodifiableMap(this.paths);
	}
}