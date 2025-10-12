/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.dmg.pmml;

public interface HasLocalTransformations<E extends PMMLObject & HasLocalTransformations<E>> {

	LocalTransformations getLocalTransformations();

	E setLocalTransformations(LocalTransformations localTransformations);
}