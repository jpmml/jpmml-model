/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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