/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.inlinetable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.dmg.pmml.Cell;
import org.dmg.pmml.NamespacePrefixes;
import org.dmg.pmml.Namespaces;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement (
	name = "input",
	namespace = Namespaces.JPMML_INLINETABLE
)
public class InputCell extends Cell {

	public InputCell(){
	}

	@ValueConstructor
	public InputCell(@Property("value") Object value){
		super(value);
	}

	@Override
	public QName getName(){
		return InputCell.QNAME;
	}

	public static final QName QNAME = new QName(Namespaces.JPMML_INLINETABLE, "input", NamespacePrefixes.JPMML_INLINETABLE);
}