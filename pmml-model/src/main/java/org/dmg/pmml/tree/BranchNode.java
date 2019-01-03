/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.util.ArrayList;
import java.util.List;

public class BranchNode extends SimpleNode {

	private String id = null;

	private String defaultChild = null;

	private List<Node> nodes = null;


	@Override
	public String getId(){
		return this.id;
	}

	@Override
	public SimpleNode setId(String id){
		this.id = id;

		return this;
	}

	@Override
	public String getDefaultChild(){
		return this.defaultChild;
	}

	@Override
	public BranchNode setDefaultChild(String defaultChild){
		this.defaultChild = defaultChild;

		return this;
	}

	@Override
	public boolean hasNodes(){
		return (this.nodes != null) && (this.nodes.size() > 0);
	}

	@Override
	public List<Node> getNodes(){

		if(this.nodes == null){
			this.nodes = new ArrayList<>();
		}

		return this.nodes;
	}
}