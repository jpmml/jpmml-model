/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Field extends PMMLObject implements HasName, Indexable<FieldName> {

	@Override
	abstract
	public FieldName getName();

	@Override
	abstract
	public Field setName(FieldName name);

	/**
	 * @throws UnsupportedOperationException If the <code>displayName</code> attribute is not supported.
	 */
	abstract
	public String getDisplayName();

	/**
	 * @throws UnsupportedOperationException If the <code>displayName</code> attribute is not supported.
	 */
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

	@Override
	public FieldName getKey(){
		return getName();
	}
}