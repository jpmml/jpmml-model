/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType (
	value = XmlAccessType.FIELD
)
@XmlRootElement (
	name = "Extension",
	namespace = "http://localhost/test"
)
public class LocalExtension {
}