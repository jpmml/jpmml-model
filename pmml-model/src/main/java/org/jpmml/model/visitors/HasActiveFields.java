/*
 * Copyright (c) 2020 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.Set;

public interface HasActiveFields {

	Set<String> getActiveFields();
}