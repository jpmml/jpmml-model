/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasTable<E extends PMMLObject & HasTable<E>> {

	TableLocator getTableLocator();

	E setTableLocator(TableLocator tableLocator);

	InlineTable getInlineTable();

	E setInlineTable(InlineTable inlineTable);
}