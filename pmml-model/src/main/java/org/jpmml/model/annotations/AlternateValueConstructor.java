/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.jpmml.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (
	value = RetentionPolicy.RUNTIME
)
@Target (
	value = {ElementType.CONSTRUCTOR}
)
public @interface AlternateValueConstructor {
}