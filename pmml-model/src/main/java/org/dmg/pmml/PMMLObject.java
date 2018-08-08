/*
 * Copyright (c) 2009 University of Tartu
 */
package org.dmg.pmml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.xml.sax.Locator;

@XmlTransient
abstract
public class PMMLObject implements HasLocator, Serializable, Visitable {

	@XmlTransient
	@com.sun.xml.bind.annotation.XmlLocation
	@org.eclipse.persistence.oxm.annotations.XmlLocation
	@JsonIgnore
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
	protected VisitorAction traverse(Visitor visitor, Visitable first){

		if(first != null){
			return first.accept(visitor);
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverse(Visitor visitor, Visitable first, Visitable second){

		if(first != null){
			VisitorAction status = first.accept(visitor);

			if(status != VisitorAction.CONTINUE){
				return status;
			}
		} // End if

		if(second != null){
			return second.accept(visitor);
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverse(Visitor visitor, Visitable... objects){

		for(int i = 0, max = objects.length; i < max; i++){
			Visitable visitable = objects[i];

			if(visitable != null){
				VisitorAction status = visitable.accept(visitor);

				if(status != VisitorAction.CONTINUE){
					return status;
				}
			}
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverse(Visitor visitor, List<? extends Visitable> objects){

		for(int i = 0, max = objects.size(); i < max; i++){
			Visitable visitable = objects.get(i);

			if(visitable != null){
				VisitorAction status = visitable.accept(visitor);

				if(status != VisitorAction.CONTINUE){
					return status;
				}
			}
		}

		return VisitorAction.CONTINUE;
	}

	static
	protected VisitorAction traverseMixed(Visitor visitor, List<?> objects){

		for(int i = 0, max = objects.size(); i < max; i++){
			Object object = objects.get(i);

			if(object instanceof Visitable){
				Visitable visitable = (Visitable)object;

				VisitorAction status = visitable.accept(visitor);

				if(status != VisitorAction.CONTINUE){
					return status;
				}
			}
		}

		return VisitorAction.CONTINUE;
	}
}