/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.HashSet;
import java.util.Set;

import org.dmg.pmml.Field;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.mining.MiningModel;

abstract
class ModelCleaner extends DeepFieldResolver {

	private Set<Field<?>> activeFields = new HashSet<>();


	@Override
	public void applyTo(Visitable visitable){
		this.activeFields.clear();

		super.applyTo(visitable);
	}

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof MiningModel){
			MiningModel miningModel = (MiningModel)parent;

			processMiningModel(miningModel);
		} else

		if(parent instanceof Model){
			Model model = (Model)parent;

			processModel(model);
		}

		return parent;
	}

	private void processMiningModel(MiningModel miningModel){
		Set<Field<?>> activeFields = getActiveFields();

		activeFields.addAll(DeepFieldResolverUtil.getActiveFields(this, miningModel));
	}

	private void processModel(Model model){
		Set<Field<?>> activeFields = getActiveFields();

		activeFields.addAll(DeepFieldResolverUtil.getActiveFields(this, model));
	}

	public Set<Field<?>> getActiveFields(){
		return this.activeFields;
	}
}