/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml;

/**
 * <p>
 * A marker interface for custom types, which can be &quot;unwrapped&quot; to primitive XML schema data type.
 * </p>
 *
 * @see HasValue
 * @see HasValueSet
 */
public interface ComplexValue {

	/**
	 * @return A non-<code>null</code> value,
	 * which can be converted to String representation via {@link Object#toString()}.
	 */
	Object toSimpleValue();
}