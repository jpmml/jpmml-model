/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import java.lang.annotation.*;

/**
 * The PMML schema version of inclusion.
 *
 * @see Removed
 */
@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.TYPE, ElementType.FIELD}
)
public @interface Added {

	Version value() default Version.PMML_3_0;
}