/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.inlinetable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.dmg.pmml.Cell;
import org.dmg.pmml.Namespaces;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement (
	name = "output",
	namespace = Namespaces.JPMML_INLINETABLE
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

	public static final QName QNAME = new QName(Namespaces.JPMML_INLINETABLE, "output", "data");
}