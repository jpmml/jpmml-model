/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.annotation.*;

/**
 * The PMML schema version of exclusion.
 *
 * @see Added
 */
@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.TYPE, ElementType.FIELD}
)
public @interface Removed {

	Version value() default Version.PMML_4_2;
}