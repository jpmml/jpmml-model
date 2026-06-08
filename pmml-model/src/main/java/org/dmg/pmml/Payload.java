/*
 * Copyright (c) 2026 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Payload<E extends Payload<E>> extends PMMLObject implements HasTargetFieldReference<E> {
}