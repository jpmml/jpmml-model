/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayList;
import java.util.List;

import org.dmg.pmml.Visitable;
import org.jpmml.model.MarkupException;

/**
 * <p>
 * This class provides a skeletal implementation of class model inspectors.
 * </p>
 *
 * <p>
 * Unlike evaluation, which takes place in "dynamic mode", the inspection takes place in "static mode".
 * The inspector performs the full traversal of the specified class model object.
 * Every problematic PMML element or attribute is reported in the form of an appropriate {@link MarkupException} instance.
 * The class model object can be considered safe and sound if the {@link #getExceptions() list of exceptions} stays empty.
 * </p>
 *
 * Typical usage:
 * <pre>
 * static
 * public &lt;E extends MarkupException&gt; void inspect(MarkupInspector&lt;E&gt; inspector){
 *   Visitable visitable = ...;
 *
 *   try {
 *     inspector.applyTo(visitable);
 *   } catch(MarkupException me){
 *     List&lt;E&gt; exceptions = inspector.getException();
 *   }
 * }
 * </pre>
 */
abstract
public class MarkupInspector<E extends MarkupException> extends AbstractVisitor {

	private List<E> exceptions = new ArrayList<>();


	/**
	 * @throws E The first element of the {@link #getExceptions() list of Exceptions} if this list is not empty.
	 */
	@Override
	public void applyTo(Visitable visitable){
		super.applyTo(visitable);

		List<E> exceptions = getExceptions();
		if(!exceptions.isEmpty()){
			throw exceptions.get(0);
		}
	}

	protected void report(E exception){
		this.exceptions.add(exception);
	}

	public List<E> getExceptions(){
		return this.exceptions;
	}
}