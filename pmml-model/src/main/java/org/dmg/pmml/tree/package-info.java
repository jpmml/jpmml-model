/*
 * Copyright (c) 2019 Villu Ruusmann
 */
@XmlJavaTypeAdapters (
	value = {
		@XmlJavaTypeAdapter (
			value = NodeAdapter.class,
			type = Node.class
		)
	}
)
package org.dmg.pmml.tree;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.dmg.pmml.adapters.NodeAdapter;