/*
 * Copyright (c) 2016 Villu Ruusmann
 */
@XmlSchema (
	xmlns = {
		@XmlNs (
			prefix = "",
			namespaceURI = NamespaceURIs.PMML_LATEST
		),
		@XmlNs (
			prefix = "data",
			namespaceURI = NamespaceURIs.JPMML_INLINETABLE
		)
	},
	namespace = NamespaceURIs.PMML_LATEST,
	elementFormDefault = XmlNsForm.UNQUALIFIED
)
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlNs;
import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
