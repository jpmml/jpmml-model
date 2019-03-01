/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.jpmml.model.annotations.Property;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "",
	propOrder = {"value"}
)
@XmlTransient
abstract
public class Cell extends PMMLObject {

	@XmlValue
	@XmlValueExtension
	private String value = null;


	public Cell(){
	}

	public Cell(@Property("value") String value){
		this.value = value;
	}

	abstract
	public QName getName();

	public String getValue(){
		return this.value;
	}

	public Cell setValue(@Property("value") String value){
		this.value = value;

		return this;
	}

	@Override
	public VisitorAction accept(Visitor visitor){
		VisitorAction status = visitor.visit(this);

		if(status == VisitorAction.CONTINUE){
			visitor.pushParent(this);
			visitor.popParent();
		} // End if

		if(status == VisitorAction.TERMINATE){
			return VisitorAction.TERMINATE;
		}

		return VisitorAction.CONTINUE;
	}
}