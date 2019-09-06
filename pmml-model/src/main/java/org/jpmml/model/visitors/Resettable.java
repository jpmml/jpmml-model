/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model.visitors;

/**
 * <p>
 * A marker interface for Visitors that accrue state,
 * and therefore should be reset to the initial (empty-) state when applying to multiple PMML elements.
 * </p>
 *
 * <pre>
 * Visitor visitor = ...;
 * Collection&lt;PMMLElement&gt; objects = ...;
 * for(PMMLElement object : objects){
 *   if(visitor instanceof Resettable){
 *     Resettable resettable = (Resettable)visitor;
 *     resettable.reset();
 *   }
 *   visitor.applyTo(object);
 * }
 * </pre>
 */
public interface Resettable {

	void reset();
}