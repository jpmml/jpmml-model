/*
 * Copyright (c) 2017 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasTable {

	TableLocator getTableLocator();

	HasTable setTableLocator(TableLocator tableLocator);

	InlineTable getInlineTable();

	HasTable setInlineTable(InlineTable inlineTable);
}