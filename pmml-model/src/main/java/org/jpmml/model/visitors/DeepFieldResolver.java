/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import org.dmg.pmml.Visitable;

abstract
public class DeepFieldResolver extends FieldResolver {

	private FieldDependencyResolver fieldDependencyResolver = null;


	@Override
	public void applyTo(Visitable visitable){
		FieldDependencyResolver fieldDependencyResolver = new FieldDependencyResolver();
		fieldDependencyResolver.applyTo(visitable);

		setFieldDependencyResolver(fieldDependencyResolver);

		super.applyTo(visitable);
	}

	public FieldDependencyResolver getFieldDependencyResolver(){
		return this.fieldDependencyResolver;
	}

	private void setFieldDependencyResolver(FieldDependencyResolver fieldDependencyResolver){
		this.fieldDependencyResolver = fieldDependencyResolver;
	}
}