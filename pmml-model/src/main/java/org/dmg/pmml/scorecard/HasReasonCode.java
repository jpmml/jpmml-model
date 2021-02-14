/*
 * Copyright (c) 2021 Villu Ruusmann
 */
package org.dmg.pmml.scorecard;

import org.dmg.pmml.PMMLObject;

/**
 * <p>
 * A marker interface for PMML elements that specify the <code>reasonCode</code> attribute.
 * </p>
 */
public interface HasReasonCode<E extends PMMLObject & HasReasonCode<E>> {

	String getReasonCode();

	E setReasonCode(String reasonCode);
}