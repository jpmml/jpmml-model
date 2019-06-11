/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.inlinetable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import org.dmg.pmml.Cell;
import org.jpmml.model.annotations.Property;
import org.jpmml.model.annotations.ValueConstructor;

@XmlRootElement (
	name = "input",
	namespace = "http://jpmml.org/jpmml-model/InlineTable"
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

	public static final QName QNAME = new QName("http://jpmml.org/jpmml-model/InlineTable", "input", "data");
}