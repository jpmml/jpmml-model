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
 * Marks a feature that was changed from required to optional in the specified PMML schema version.
 *
 * @see Required
 */
@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.TYPE, ElementType.FIELD}
)
public @interface Optional {

	Version value();
}