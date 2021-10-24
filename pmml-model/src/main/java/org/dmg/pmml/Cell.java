/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.namespace.QName;

import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.adapters.ObjectAdapter;
import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

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