/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.dmg.pmml;

import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
abstract
public class Field<E extends Field<E>> extends PMMLObject implements HasRequiredName<E>, HasDisplayName<E>, HasType<E> {
}