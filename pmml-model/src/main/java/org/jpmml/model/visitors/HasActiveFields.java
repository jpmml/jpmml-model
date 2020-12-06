/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Set;

import org.dmg.pmml.FieldName;

public interface HasActiveFields {

	Set<FieldName> getActiveFields();
}