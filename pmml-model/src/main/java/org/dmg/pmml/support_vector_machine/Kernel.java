/*
 * Copyright (c) 2013 KNIME.com AG, Zurich, Switzerland
 */
package org.dmg.pmml.support_vector_machine;

import jakarta.xml.bind.annotation.XmlTransient;
import org.dmg.pmml.PMMLObject;

@XmlTransient
abstract
public class Kernel extends PMMLObject {

	abstract
	public String getDescription();

	abstract
	public Kernel setDescription(String description);
}