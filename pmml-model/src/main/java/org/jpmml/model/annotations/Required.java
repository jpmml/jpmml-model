/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dmg.pmml.Version;

/**
 * Marks a feature that was changed from optional to required in the specified PMML schema version.
 *
 * @see Optional
 */
@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.TYPE, ElementType.FIELD}
)
public @interface Required {

	Version value();
}