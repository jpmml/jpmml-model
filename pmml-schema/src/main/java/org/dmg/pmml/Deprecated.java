/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.annotation.*;

@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.TYPE, ElementType.FIELD}
)
public @interface Deprecated {

	Version value() default Version.PMML_4_2;
}