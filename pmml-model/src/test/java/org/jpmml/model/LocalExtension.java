/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType (
	value = XmlAccessType.FIELD
)
@XmlRootElement (
	name = "Extension",
	namespace = "http://localhost/test"
)
public class LocalExtension {
}