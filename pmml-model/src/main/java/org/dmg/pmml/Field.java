/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Field extends PMMLObject implements HasName<Field>, Indexable<FieldName> {

	public String getDisplayName(){
		return null;
	}

	/**
	 * @throws UnsupportedOperationException If the <code>displayName</code> attribute is not supported.
	 */
	public Field setDisplayName(String displayName){
		throw new UnsupportedOperationException();
	}

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