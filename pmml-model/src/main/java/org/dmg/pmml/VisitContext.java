/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Deque;

interface VisitContext {

	Deque<PMMLObject> getParents();

	default
	public void pushParent(PMMLObject parent){
		Deque<PMMLObject> parents = getParents();

		parents.push(parent);
	}

	default
	public PMMLObject popParent(){
		Deque<PMMLObject> parents = getParents();

		return parents.pop();
	}
}