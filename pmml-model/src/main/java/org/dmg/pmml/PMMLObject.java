/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.xml.sax.Locator;

@XmlTransient
abstract
public class PMMLObject implements HasLocator, Serializable, Visitable {

	@XmlTransient
	@com.sun.xml.bind.annotation.XmlLocation
	@org.eclipse.persistence.oxm.annotations.XmlLocation
	private Locator locator;


	@Override
	public Locator getLocator(){
		return this.locator;
	}

	@Override
	public void setLocator(Locator locator){
		this.locator = locator;
	}

	static
	VisitorAction traverse(Visitor visitor, Visitable... objects){
		return traverse(visitor, Arrays.asList(objects));
	}

	static
	VisitorAction traverse(Visitor visitor, List<? extends Visitable> objects){

		for(int i = 0; i < objects.size(); i++){
			Visitable visitable = objects.get(i);

			if(visitable == null){
				continue;
			}

			VisitorAction status = visitable.accept(visitor);
			if(status != VisitorAction.CONTINUE){
				return status;
			}
		}

		return VisitorAction.CONTINUE;
	}

	static
	VisitorAction traverseMixed(Visitor visitor, List<?> objects){

		for(int i = 0; i < objects.size(); i++){
			Object object = objects.get(i);

			if(!(object instanceof Visitable)){
				continue;
			}

			Visitable visitable = (Visitable)object;

			VisitorAction status = visitable.accept(visitor);
			if(status != VisitorAction.CONTINUE){
				return status;
			}
		}

		return VisitorAction.CONTINUE;
	}
}