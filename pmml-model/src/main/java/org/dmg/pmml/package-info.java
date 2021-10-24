/*
 * Copyright (c) 2016 Villu Ruusmann
 */
@XmlSchema (
	xmlns = {
		@XmlNs (
			prefix = "",
			namespaceURI = "http://www.dmg.org/PMML-4_4"
		),
		@XmlNs (
			prefix = "data",
			namespaceURI = "http://jpmml.org/jpmml-model/InlineTable"
		)
	},
	namespace = "http://www.dmg.org/PMML-4_4",
	elementFormDefault = XmlNsForm.UNQUALIFIED
)
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
