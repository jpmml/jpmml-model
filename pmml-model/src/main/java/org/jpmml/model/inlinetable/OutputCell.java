/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.inlinetable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.dmg.pmml.Cell;
import org.jpmml.model.annotations.Property;

@XmlRootElement (
	name = "output",
	namespace = "http://jpmml.org/jpmml-model/InlineTable"
)
public class OutputCell extends Cell {

	public OutputCell(){
	}

	public OutputCell(@Property("value") Object value){
		super(value);
	}

	@Override
	public QName getName(){
		return OutputCell.QNAME;
	}

	private static final QName QNAME = new QName("http://jpmml.org/jpmml-model/InlineTable", "output", "data");
}