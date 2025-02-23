/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JJavaName;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.util.CodeModelClassFactory;
import org.glassfish.jaxb.core.api.impl.NameConverter;
import org.xml.sax.ErrorHandler;

public class VisitorPlugin extends Plugin {

	@Override
	public String getOptionName(){
		return "Xvisitor";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		CodeModelClassFactory clazzFactory = outline.getClassFactory();

		JClass objectClass = codeModel.ref(Object.class);

		JClass pmmlObjectClass = codeModel.ref("org.dmg.pmml.PMMLObject");
		JClass visitableInterface = codeModel.ref("org.dmg.pmml.Visitable");
		JClass visitContextInterface = codeModel.ref("org.dmg.pmml.VisitContext");
		JClass visitorActionEnum = codeModel.ref("org.dmg.pmml.VisitorAction");

		JClass dequeClass = codeModel.ref(Deque.class);
		JClass dequeImplementationClass = codeModel.ref(ArrayDeque.class);

		JFieldRef continueAction = visitorActionEnum.staticRef("CONTINUE");
		JFieldRef skipAction = visitorActionEnum.staticRef("SKIP");
		JFieldRef terminateAction = visitorActionEnum.staticRef("TERMINATE");

		JPackage pmmlPackage = pmmlObjectClass._package();

		JDefinedClass visitorInterface = clazzFactory.createClass(pmmlPackage, JMod.PUBLIC, "Visitor", null, ClassType.INTERFACE);
		visitorInterface._implements(visitContextInterface);

		JMethod visitorApplyTo = visitorInterface.method(JMod.PUBLIC, void.class, "applyTo");
		visitorApplyTo.javadoc().append("@see Visitable#accept(Visitor)");
		visitorApplyTo.param(visitableInterface, "visitable");

		JPackage visitorPackage = codeModel._package("org.jpmml.model.visitors");

		JDefinedClass abstractVisitorClazz = clazzFactory.createClass(visitorPackage, JMod.ABSTRACT | JMod.PUBLIC, "AbstractVisitor", null, ClassType.CLASS);
		abstractVisitorClazz._implements(visitorInterface);

		JFieldVar abstractVisitorParents = abstractVisitorClazz.field(JMod.PRIVATE, dequeClass.narrow(pmmlObjectClass), "parents", JExpr._new(dequeImplementationClass.narrow(pmmlObjectClass)));

		JMethod abstractVisitorGetParents = abstractVisitorClazz.method(JMod.PUBLIC, dequeClass.narrow(pmmlObjectClass), "getParents");
		abstractVisitorGetParents.annotate(Override.class);

		abstractVisitorGetParents.body()._return(JExpr.refthis(abstractVisitorParents.name()));

		JMethod abstractVisitorApplyTo = abstractVisitorClazz.method(JMod.PUBLIC, void.class, "applyTo");
		abstractVisitorApplyTo.annotate(Override.class);

		JVar visitable = abstractVisitorApplyTo.param(visitableInterface, "visitable");

		abstractVisitorApplyTo.body().add(JExpr.invoke(visitable, "accept").arg(JExpr._this()));

		JMethod abstractVisitorVisit = abstractVisitorClazz.method(JMod.PUBLIC, visitorActionEnum, "visit");
		abstractVisitorVisit.param(pmmlObjectClass, "object");

		abstractVisitorVisit.body()._return(continueAction);

		BiFunction<JClass, JClass, List<JMethod>> methodGenerator = new BiFunction<JClass, JClass, List<JMethod>>(){

			@Override
			public List<JMethod> apply(JClass clazz, JClass superClazz){
				String parameterName = NameConverter.standard.toVariableName((clazz.erasure()).name());
				if(!JJavaName.isJavaIdentifier(parameterName)){
					parameterName = ("_" + parameterName);
				}

				JMethod visitorVisit = visitorInterface.method(JMod.PUBLIC, visitorActionEnum, "visit");
				visitorVisit.param(clazz, parameterName);

				JMethod abstractVisitorVisit = abstractVisitorClazz.method(JMod.PUBLIC, visitorActionEnum, "visit");
				abstractVisitorVisit.annotate(Override.class);
				abstractVisitorVisit.param(clazz, parameterName);

				abstractVisitorVisit.body()._return(JExpr.invoke("visit").arg(JExpr.cast(superClazz.erasure(), JExpr.ref(parameterName))));

				return Arrays.asList(visitorVisit, abstractVisitorVisit);
			}
		};

		Comparator<ClassOutline> comparator = new Comparator<ClassOutline>(){

			@Override
			public int compare(ClassOutline left, ClassOutline right){
				return (left.implClass.name()).compareToIgnoreCase(right.implClass.name());
			}
		};

		List<ClassOutline> classOutlines = new ArrayList<>(outline.getClasses());
		classOutlines.sort(comparator);

		Set<String> traversableTypes = new LinkedHashSet<>();

		String[][] abstractClasses = {
			{"Cell"},
			{"ComparisonField<?>"},
			{"ContinuousDistribution", "Distribution"},
			{"DiscreteDistribution", "Distribution"},
			{"Distance", "Measure"},
			{"Distribution"},
			{"EmbeddedModel"},
			{"Entity<?>"},
			{"Expression"},
			{"Field<?>"},
			{"support_vector_machine.Kernel"},
			{"Measure"},
			{"Model"},
			{"neural_network.NeuralEntity", "Entity<?>"},
			{"tree.Node", "Entity<?>"},
			{"general_regression.ParameterCell"},
			// {"PMMLObject"},
			{"Predicate"},
			{"general_regression.PredictorList"},
			{"rule_set.Rule", "Entity<?>"},
			{"ScoreDistribution"},
			{"Similarity", "Measure"},
			{"SparseArray<?>"},
			{"regression.Term"},
			{"time_series.Algorithm"}
		};

		for(String[] abstractClass : abstractClasses){
			JClass beanClazz = codeModel.ref(getTypeName("org.dmg.pmml." + abstractClass[0]));
			traversableTypes.add(getTypeName(beanClazz));

			if((abstractClass[0]).endsWith("<?>")){
				beanClazz = beanClazz.narrow(codeModel.wildcard());
			}

			JClass beanSuperClazz = pmmlObjectClass;
			if(abstractClass.length > 1){
				beanSuperClazz = codeModel.ref(getTypeName("org.dmg.pmml." + abstractClass[1]));
			}

			methodGenerator.apply(beanClazz, beanSuperClazz);
		} // End for

		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			traversableTypes.add(getTypeName(beanClazz));

			JClass beanSuperClazz = beanClazz._extends();
			traversableTypes.add(getTypeName(beanSuperClazz));
		} // End for

		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			methodGenerator.apply(beanClazz, beanClazz._extends());

			JMethod beanAccept = beanClazz.method(JMod.PUBLIC, visitorActionEnum, "accept");
			beanAccept.annotate(Override.class);

			JVar visitorParameter = beanAccept.param(visitorInterface, "visitor");

			JBlock body = beanAccept.body();

			JVar status = body.decl(visitorActionEnum, "status", JExpr.invoke(visitorParameter, "visit").arg(JExpr._this()));

			JBlock ifBody = body._if(status.eq(continueAction))._then();

			ifBody.add(JExpr.invoke(visitorParameter, "pushParent").arg(JExpr._this()));

			JInvocation traverseVarargs = null;

			FieldOutline[] fieldsOutlines = classOutline.getDeclaredFields();
			for(FieldOutline fieldOutline : fieldsOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				if(propertyInfo instanceof CAttributePropertyInfo){
					continue;
				}

				JType fieldType = fieldOutline.getRawType();

				JMethod getterMethod = beanClazz.getMethod("get" + propertyInfo.getName(true), new JType[0]);

				// Collection of values
				if(propertyInfo.isCollection()){
					JType fieldElementType = CodeModelUtil.getElementType(fieldType);

					if(traversableTypes.contains(getTypeName(fieldElementType)) || objectClass.equals(fieldElementType)){
						JMethod hasElementsMethod = beanClazz.getMethod("has" + propertyInfo.getName(true), new JType[0]);

						ifBody._if((status.eq(continueAction)).cand(JExpr.invoke(hasElementsMethod)))._then().assign(status, pmmlObjectClass.staticInvoke(traversableTypes.contains(getTypeName(fieldElementType)) ? "traverse" : "traverseMixed").arg(visitorParameter).arg(JExpr.invoke(getterMethod)));

						traverseVarargs = null;
					}
				} else

				// Simple value
				{
					if(traversableTypes.contains(getTypeName(fieldType))){

						if(traverseVarargs == null){
							traverseVarargs = pmmlObjectClass.staticInvoke("traverse").arg(visitorParameter);

							ifBody._if(status.eq(continueAction))._then().assign(status, traverseVarargs);
						}

						traverseVarargs.arg(JExpr.invoke(getterMethod));
					}
				}
			}

			ifBody.add(JExpr.invoke(visitorParameter, "popParent"));

			body._if(status.eq(terminateAction))._then()._return(terminateAction);

			body._return(continueAction);
		}

		return true;
	}

	static
	private String getTypeName(JType type){
		return getTypeName(type.name());
	}

	static
	private String getTypeName(String name){
		int lt = name.indexOf("<");

		if(lt > -1){
			name = name.substring(0, lt);
		}

		return name;
	}
}