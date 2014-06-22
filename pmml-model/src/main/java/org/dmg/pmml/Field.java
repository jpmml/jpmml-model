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
	public void setName(FieldName name);

	abstract
	public String getDisplayName();

	abstract
	public void setDisplayName(String displayName);

	abstract
	public OpType getOptype();

	abstract
	public void setOptype(OpType opType);

	abstract
	public DataType getDataType();

	abstract
	public void setDataType(DataType dataType);
}