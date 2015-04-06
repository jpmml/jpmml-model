/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Field extends PMMLObject implements HasName {

	@Override
	abstract
	public FieldName getName();

	@Override
	abstract
	public Field setName(FieldName name);

	abstract
	public String getDisplayName();

	abstract
	public Field setDisplayName(String displayName);

	abstract
	public OpType getOpType();

	abstract
	public Field setOpType(OpType opType);

	abstract
	public DataType getDataType();

	abstract
	public Field setDataType(DataType dataType);
}