/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.DecisionTree;
import org.dmg.pmml.DefineFunction;
import org.dmg.pmml.Field;
import org.dmg.pmml.LocalTransformations;
import org.dmg.pmml.Model;
import org.dmg.pmml.Output;
import org.dmg.pmml.PMML;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.ParameterField;
import org.dmg.pmml.Regression;
import org.dmg.pmml.ResultField;
import org.dmg.pmml.Segment;
import org.dmg.pmml.Segmentation;
import org.dmg.pmml.TransformationDictionary;
import org.dmg.pmml.Visitable;
import org.dmg.pmml.VisitorAction;

/**
 * <p>
 * A Visitor that determines which fields are visible and accessible (aka &quot;in scope&quot;) at the specified location of a class model object.
 * </p>
 *
 * @see <a href="http://dmg.org/pmml/v4-2-1/FieldScope.html">Scope of Fields</a>.
 */
public class FieldResolver extends AbstractModelVisitor {

	private Map<PMMLObject, Set<Field>> scopes = new LinkedHashMap<>();

	private Set<Field> suppressedFields = new HashSet<>();


	@Override
	public void applyTo(Visitable visitable){
		this.scopes.clear();

		this.suppressedFields.clear();

		super.applyTo(visitable);
	}

	@Override
	public PMMLObject popParent(){
		PMMLObject parent = super.popParent();

		if(parent instanceof Field){
			Field field = (Field)parent;

			this.suppressedFields.remove(field);
		}

		return parent;
	}

	@Override
	public VisitorAction visit(Model model){
		LocalTransformations localTransformations = model.getLocalTransformations();
		if(localTransformations != null && localTransformations.hasDerivedFields()){
			declare(model, localTransformations.getDerivedFields());
		}

		return VisitorAction.CONTINUE;
	}

	@Override
	public VisitorAction visit(DecisionTree decisionTree){
		throw new UnsupportedOperationException();
	}

	@Override
	public VisitorAction visit(DefineFunction defineFunction){
		declare(defineFunction, defineFunction.hasParameterFields() ? defineFunction.getParameterFields() : Collections.<ParameterField>emptyList());

		return super.visit(defineFunction);
	}

	@Override
	public VisitorAction visit(LocalTransformations localTransformations){

		if(localTransformations.hasDerivedFields()){
			suppress(localTransformations.getDerivedFields());
		}

		return super.visit(localTransformations);
	}

	@Override
	public VisitorAction visit(Output output){

		if(output.hasOutputFields()){
			declare(output, output.getOutputFields());

			suppress(output.getOutputFields());
		}

		return super.visit(output);
	}

	@Override
	public VisitorAction visit(PMML pmml){
		DataDictionary dataDictionary = pmml.getDataDictionary();
		if(dataDictionary != null && dataDictionary.hasDataFields()){
			declare(pmml, dataDictionary.getDataFields());
		}

		TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();
		if(transformationDictionary != null && transformationDictionary.hasDerivedFields()){
			declare(pmml, transformationDictionary.getDerivedFields());
		}

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

		if(transformationDictionary.hasDerivedFields()){
			suppress(transformationDictionary.getDerivedFields());
		}

		return super.visit(transformationDictionary);
	}

	public Set<Field> getFields(){
		Deque<PMMLObject> parents = getParents();

		return getFields(parents);
	}

	public Set<Field> getFields(PMMLObject... virtualParents){
		Deque<PMMLObject> parents = new ArrayDeque<>(getParents());

		for(PMMLObject virtualParent : virtualParents){
			parents.push(virtualParent);
		}

		return getFields(parents);
	}

	private Set<Field> getFields(Deque<PMMLObject> parents){
		Set<Field> result = new LinkedHashSet<>();

		PMMLObject prevParent = null;

		for(Iterator<PMMLObject> it = parents.iterator(); it.hasNext(); ){
			PMMLObject parent = it.next();

			{
				Set<Field> scope = this.scopes.get(parent);

				if(scope != null){
					result.addAll(scope);
				}
			}

			if(parent instanceof DefineFunction){
				break;
			} // End if

			if((parent instanceof Segmentation) && ((prevParent == null) || (prevParent instanceof Segment))){
				List<Output> outputs = getEarlierOutputs((Segmentation)parent, (Segment)prevParent);

				for(Output output : outputs){
					Set<Field> scope = this.scopes.get(output);

					if(scope != null){
						result.addAll(scope);
					}
				}
			}

			prevParent = parent;
		}

		result.removeAll(this.suppressedFields);

		return result;
	}

	private void declare(PMMLObject object, Collection<? extends Field> fields){
		Set<Field> scope = this.scopes.get(object);

		if(scope == null){
			scope = new LinkedHashSet<>();

			this.scopes.put(object, scope);
		}

		scope.addAll(fields);
	}

	private void suppress(Collection<? extends Field> fields){
		this.suppressedFields.clear();
		this.suppressedFields.addAll(fields);
	}

	static
	private List<Output> getEarlierOutputs(Segmentation segmentation, Segment targetSegment){
		List<Output> result = new ArrayList<>();

		Segmentation.MultipleModelMethodType multipleModelMethod = segmentation.getMultipleModelMethod();
		switch(multipleModelMethod){
			case MODEL_CHAIN:
				break;
			default:
				return Collections.emptyList();
		}

		List<Segment> segments = segmentation.getSegments();
		for(Segment segment : segments){
			Model model = segment.getModel();

			if(targetSegment != null && (targetSegment).equals(segment)){
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