/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.cells;

import javax.xml.namespace.QName;

import jakarta.xml.bind.annotation.XmlRootElement;
import org.dmg.pmml.Cell;
import org.dmg.pmml.NamespacePrefixes;
import org.dmg.pmml.NamespaceURIs;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement (
	name = "input",
	namespace = NamespaceURIs.JPMML_INLINETABLE
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

	public static final QName QNAME = new QName(NamespaceURIs.JPMML_INLINETABLE, "input", NamespacePrefixes.JPMML_INLINETABLE);
}