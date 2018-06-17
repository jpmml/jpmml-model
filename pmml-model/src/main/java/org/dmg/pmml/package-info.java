/*
 * Copyright (c) 2016 Villu Ruusmann
 */
@XmlSchema (
	xmlns = {
		@XmlNs (
			prefix = "",
			namespaceURI = "http://www.dmg.org/PMML-4_3"
		),
		@XmlNs (
			prefix = "data",
			namespaceURI = "http://jpmml.org/jpmml-model/InlineTable"
		)
	},
	namespace = "http://www.dmg.org/PMML-4_3",
	elementFormDefault = XmlNsForm.UNQUALIFIED
)
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
