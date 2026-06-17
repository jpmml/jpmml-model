/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dmg.pmml.adapters.PayloadAdapter;

@XmlTransient
@XmlJavaTypeAdapter (
	value = PayloadAdapter.class
)
abstract
public class Payload<E extends Payload<E>> extends PMMLObject implements HasTargetFieldReference<E> {
}