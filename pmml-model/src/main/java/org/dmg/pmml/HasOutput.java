/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasOutput<E extends PMMLObject & HasOutput<E>> {

	Output getOutput();

	E setOutput(Output output);
}