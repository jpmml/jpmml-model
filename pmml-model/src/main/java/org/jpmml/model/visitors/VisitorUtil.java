/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Deque;
import java.util.Iterator;

import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitor;

public class VisitorUtil {

	private VisitorUtil(){
	}

	static
	public boolean isDictionary(PMMLObject parent){

		if((parent instanceof TransformationDictionary) || (parent instanceof LocalTransformations)){
			return true;
		}

		return false;
	}

	static
	public PMMLObject getParent(Visitor visitor){
		Deque<PMMLObject> parents = visitor.getParents();

		return parents.getFirst();
	}

	static
	public PMMLObject getParent(Visitor visitor, int index){
		Deque<PMMLObject> parents = visitor.getParents();

		if(index < 0){
			throw new IllegalArgumentException();
		} else

		if(index == 0){
			return parents.getFirst();
		}

		Iterator<PMMLObject> it = parents.iterator();
		for(int i = 0; i < index; i++){
			it.next();
		}

		return it.next();
	}
}