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
	name = "output",
	namespace = NamespaceURIs.JPMML_INLINETABLE
)
public class OutputCell extends Cell {

	public OutputCell(){
	}

	@ValueConstructor
	public OutputCell(@Property("value") Object value){
		super(value);
	}

	@Override
	public QName getName(){
		return OutputCell.QNAME;
	}

	public static final QName QNAME = new QName(NamespaceURIs.JPMML_INLINETABLE, "output", NamespacePrefixes.JPMML_INLINETABLE);
}