/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DefineFunction;
import org.dmg.pmml.Field;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.Output;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.ResultField;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.VisitorAction;
import org.dmg.pmml.mining.Segment;
import org.dmg.pmml.mining.Segmentation;
import org.dmg.pmml.regression.Regression;
import org.dmg.pmml.tree.DecisionTree;

/**
 * <p>
 * A Visitor that determines which fields are visible and accessible (aka &quot;in scope&quot;) at the specified location of a class model object.
 * </p>
 *
 * @see <a href="http://dmg.org/pmml/v4-3/FieldScope.html">Scope of Fields</a>
 */
public class FieldResolver extends AbstractVisitor implements Resettable {

	private Map<PMMLObject, List<Field<?>>> scopes = new IdentityHashMap<>();

	private Map<PMMLObject, List<Field<?>>> customScopes = Collections.emptyMap();


	@Override
	public void reset(){
		this.scopes.clear();

		this.customScopes = Collections.emptyMap();
	}

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof Field){
			Field<?> field = (Field<?>)parent;

			parent = getParent();

			List<Field<?>> customScope = this.customScopes.get(parent);
			if(customScope != null){
				customScope.add(field);
			}
		} else

		if(parent instanceof TransformationDictionary){
			PMML pmml = (PMML)getParent();

			declareGlobalFields(pmml, true);

			this.customScopes = Collections.emptyMap();
		} else

		if(parent instanceof LocalTransformations){
			Model model = (Model)getParent();

			declareLocalFields(model, true);

			this.customScopes = Collections.emptyMap();
		} else

		{
			List<Field<?>> customScope = this.customScopes.get(parent);
			if(customScope != null){
				this.customScopes = Collections.emptyMap();
			}
		}

		return parent;
	}

	@Override
	public VisitorAction visit(Model model){
		declareLocalFields(model, true);

		return super.visit(model);
	}

	@Override
	public VisitorAction visit(DecisionTree decisionTree){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(DefineFunction defineFunction){
		declareFields(defineFunction, defineFunction.hasParameterFields() ? defineFunction.getParameterFields() : Collections.emptyList());

		return super.visit(defineFunction);
	}

	@Override
	public VisitorAction visit(LocalTransformations localTransformations){
		Model model = (Model)getParent();

		if(localTransformations.hasDerivedFields()){
			declareLocalFields(model, false);

			suppressFields(localTransformations);
		}

		return super.visit(localTransformations);
	}

	@Override
	public VisitorAction visit(Output output){

		if(output.hasOutputFields()){
			declareFields(output, output.getOutputFields());

			suppressFields(output);
		}

		return super.visit(output);
	}

	@Override
	public VisitorAction visit(PMML pmml){
		declareGlobalFields(pmml, true);

		return super.visit(pmml);
	}

	@Override
	public VisitorAction visit(Regression regression){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(ResultField resultField){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(TransformationDictionary transformationDictionary){
		PMML pmml = (PMML)getParent();

		if(transformationDictionary.hasDerivedFields()){
			declareGlobalFields(pmml, false);

			suppressFields(transformationDictionary);
		}

		return super.visit(transformationDictionary);
	}

	public Collection<Field<?>> getFields(){
		Deque<PMMLObject> parents = getParents();

		return getFields(parents);
	}

	public Collection<Field<?>> getFields(PMMLObject... virtualParents){
		Deque<PMMLObject> parents = new ArrayDeque<>(getParents());

		for(PMMLObject virtualParent : virtualParents){
			parents.push(virtualParent);
		}

		return getFields(parents);
	}

	private Collection<Field<?>> getFields(Deque<PMMLObject> parents){
		List<Field<?>> result = new ArrayList<>();

		PMMLObject prevParent = null;

		for(PMMLObject parent : parents){

			{
				List<Field<?>> scope = getScope(parent);

				if(scope != null && !scope.isEmpty()){
					result.addAll(scope);
				}
			}

			if(parent instanceof DefineFunction){
				break;
			} // End if

			if((parent instanceof Segmentation) && ((prevParent == null) || (prevParent instanceof Segment))){
				List<Output> outputs = getEarlierOutputs((Segmentation)parent, (Segment)prevParent);

				for(Output output : outputs){
					List<Field<?>> scope = getScope(output);

					if(scope != null && !scope.isEmpty()){
						result.addAll(scope);
					}
				}
			}

			prevParent = parent;
		}

		return result;
	}

	private List<Field<?>> getScope(PMMLObject object){

		if(!this.customScopes.isEmpty()){
			List<Field<?>> customScope = this.customScopes.get(object);

			if(customScope != null){
				return customScope;
			}
		}

		return this.scopes.get(object);
	}

	private void declareGlobalFields(PMML pmml, boolean transformations){
		List<Field<?>> scope = this.scopes.get(pmml);

		if(scope != null){
			scope.clear();
		}

		DataDictionary dataDictionary = pmml.requireDataDictionary();
		if(dataDictionary.hasDataFields()){
			declareFields(pmml, dataDictionary.getDataFields());
		}

		TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();
		if(transformations && (transformationDictionary != null && transformationDictionary.hasDerivedFields())){
			declareFields(pmml, transformationDictionary.getDerivedFields());
		}
	}

	private void declareLocalFields(Model model, boolean transformations){
		List<Field<?>> scope = this.scopes.get(model);

		if(scope != null){
			scope.clear();
		}

		LocalTransformations localTransformations = model.getLocalTransformations();
		if(transformations && (localTransformations != null && localTransformations.hasDerivedFields())){
			declareFields(model, localTransformations.getDerivedFields());
		}
	}

	private void declareFields(PMMLObject object, Collection<? extends Field<?>> fields){
		List<Field<?>> scope = this.scopes.get(object);

		if(scope == null){
			scope = new ArrayList<>(fields.size());

			this.scopes.put(object, scope);
		}

		scope.addAll(fields);
	}

	private void suppressFields(PMMLObject object){
		this.customScopes = Collections.singletonMap(object, new ArrayList<>());
	}

	static
	private List<Output> getEarlierOutputs(Segmentation segmentation, Segment targetSegment){
		List<Output> result = new ArrayList<>();

		Segmentation.MultipleModelMethod multipleModelMethod = segmentation.requireMultipleModelMethod();
		switch(multipleModelMethod){
			case MODEL_CHAIN:
			case MULTI_MODEL_CHAIN:
				break;
			default:
				return Collections.emptyList();
		}

		List<Segment> segments = segmentation.requireSegments();
		for(Segment segment : segments){
			Model model = segment.requireModel();

			if(targetSegment != null && Objects.equals(segment, targetSegment)){
				break;
			}

			Output output = model.getOutput();
			if(output != null){
				result.add(output);
			}
		}

		return result;
	}
}