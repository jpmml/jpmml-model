/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.Deque;
import java.util.Iterator;

interface VisitContext {

	Deque<PMMLObject> getParents();

	default
	public boolean hasParent(){
		Deque<PMMLObject> parents = getParents();

		return !parents.isEmpty();
	}

	default
	public PMMLObject getParent(){
		Deque<PMMLObject> parents = getParents();

		return parents.getFirst();
	}

	default
	public PMMLObject getParent(int index){
		Deque<PMMLObject> parents = getParents();

		if(index < 0){
			throw new IllegalArgumentException();
		}

		Iterator<PMMLObject> it = parents.iterator();
		for(int i = 0; i < index; i++){
			it.next();
		}

		return it.next();
	}

	default
	public void pushParent(PMMLObject parent){
		Deque<PMMLObject> parents = getParents();

		parents.addFirst(parent);
	}

	default
	public PMMLObject popParent(){
		Deque<PMMLObject> parents = getParents();

		return parents.removeFirst();
	}
}