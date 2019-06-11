/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import org.dmg.pmml.adapters.ObjectAdapter;
import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType (
	name = "",
	propOrder = {"value"}
)
@XmlTransient
abstract
public class Cell extends PMMLObject {

	@XmlValue
	@XmlJavaTypeAdapter(ObjectAdapter.class)
	@XmlSchemaType(name = "anySimpleType")
	@XmlValueExtension
	private Object value = null;


	public Cell(){
	}

	@ValueConstructor
	public Cell(@Property("value") Object value){
		this.value = value;
	}

	abstract
	public QName getName();

	public Object getValue(){
		return this.value;
	}

	public Cell setValue(@Property("value") Object value){
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