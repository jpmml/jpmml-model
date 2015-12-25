/*
 * Copyright (c) 2013 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
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
import com.sun.istack.build.NameConverter;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.tools.xjc.util.CodeModelClassFactory;
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

		JClass objectClazz = codeModel.ref(Object.class);

		JClass pmmlObjectClazz = codeModel.ref("org.dmg.pmml.PMMLObject");
		JClass visitableInterface = codeModel.ref("org.dmg.pmml.Visitable");

		JClass dequeClazz = codeModel.ref(Deque.class);
		JClass dequeImplementationClazz = codeModel.ref(ArrayDeque.class);

		JPackage pmmlPackage = pmmlObjectClazz._package();

		JDefinedClass visitorActionClazz = clazzFactory.createClass(pmmlPackage, JMod.PUBLIC, "VisitorAction", null, ClassType.ENUM);
		JEnumConstant continueAction = visitorActionClazz.enumConstant("CONTINUE");
		JEnumConstant skipAction = visitorActionClazz.enumConstant("SKIP");
		JEnumConstant terminateAction = visitorActionClazz.enumConstant("TERMINATE");

		JDefinedClass visitorInterface = clazzFactory.createClass(pmmlPackage, JMod.PUBLIC, "Visitor", null, ClassType.INTERFACE);

		JMethod visitorApplyTo = visitorInterface.method(JMod.PUBLIC, void.class, "applyTo");
		visitorApplyTo.javadoc().append("@see Visitable#accept(Visitor)");
		visitorApplyTo.param(visitableInterface, "visitable");

		JMethod visitorPushParent = visitorInterface.method(JMod.PUBLIC, void.class, "pushParent");
		visitorPushParent.param(pmmlObjectClazz, "object");

		JMethod visitorPopParent = visitorInterface.method(JMod.PUBLIC, pmmlObjectClazz, "popParent");

		JMethod visitorGetParents = visitorInterface.method(JMod.PUBLIC, dequeClazz.narrow(pmmlObjectClazz), "getParents");

		JPackage visitorPackage = codeModel._package("org.jpmml.model.visitors");

		JDefinedClass abstractVisitorClazz = clazzFactory.createClass(visitorPackage, JMod.ABSTRACT | JMod.PUBLIC, "AbstractVisitor", null, ClassType.CLASS)._implements(visitorInterface);

		JFieldVar abstractVisitorParents = abstractVisitorClazz.field(JMod.PRIVATE, dequeClazz.narrow(pmmlObjectClazz), "parents", JExpr._new(dequeImplementationClazz.narrow(pmmlObjectClazz)));

		JFieldRef abstractVisitorParentsRef = JExpr.refthis(abstractVisitorParents.name());

		JMethod abstractVisitorApplyTo = abstractVisitorClazz.method(JMod.PUBLIC, void.class, "applyTo");
		abstractVisitorApplyTo.annotate(Override.class);
		JVar visitable = abstractVisitorApplyTo.param(visitableInterface, "visitable");
		abstractVisitorApplyTo.body().add(JExpr.invoke(visitable, "accept").arg(JExpr._this()));

		JMethod abstractVisitorPushParent = abstractVisitorClazz.method(JMod.PUBLIC, void.class, "pushParent");
		abstractVisitorPushParent.annotate(Override.class);
		JVar parent = abstractVisitorPushParent.param(pmmlObjectClazz, "parent");
		abstractVisitorPushParent.body().add(abstractVisitorParentsRef.invoke("addFirst").arg(parent));

		JMethod abstractVisitorPopParent = abstractVisitorClazz.method(JMod.PUBLIC, pmmlObjectClazz, "popParent");
		abstractVisitorPopParent.annotate(Override.class);
		abstractVisitorPopParent.body()._return(abstractVisitorParentsRef.invoke("removeFirst"));

		JMethod abstractVisitorGetParents = abstractVisitorClazz.method(JMod.PUBLIC, dequeClazz.narrow(pmmlObjectClazz), "getParents");
		abstractVisitorGetParents.annotate(Override.class);
		abstractVisitorGetParents.body()._return(abstractVisitorParentsRef);

		JDefinedClass abstractSimpleVisitorClazz = clazzFactory.createClass(visitorPackage, JMod.ABSTRACT | JMod.PUBLIC, "AbstractSimpleVisitor", null, ClassType.CLASS)._extends(abstractVisitorClazz);

		JMethod abstractSimpleVisitorDefaultVisit = abstractSimpleVisitorClazz.method(JMod.ABSTRACT | JMod.PUBLIC, visitorActionClazz, "visit");
		abstractSimpleVisitorDefaultVisit.param(pmmlObjectClazz, "object");

		Set<String> traversableTypes = new LinkedHashSet<>();

		Collection<? extends ClassOutline> clazzes = outline.getClasses();
		for(ClassOutline clazz : clazzes){
			JDefinedClass beanClazz = clazz.implClass;
			traversableTypes.add(beanClazz.name());

			JClass beanSuperClazz = beanClazz._extends();
			traversableTypes.add(beanSuperClazz.name());
		} // End for

		for(ClassOutline clazz : clazzes){
			JDefinedClass beanClazz = clazz.implClass;

			String parameterName = NameConverter.standard.toVariableName(beanClazz.name());
			if(!JJavaName.isJavaIdentifier(parameterName)){
				parameterName = ("_" + parameterName);
			}

			JMethod visitorVisit = visitorInterface.method(JMod.PUBLIC, visitorActionClazz, "visit");
			visitorVisit.param(beanClazz, parameterName);

			JMethod abstractVisitorVisit = abstractVisitorClazz.method(JMod.PUBLIC, visitorActionClazz, "visit");
			abstractVisitorVisit.annotate(Override.class);
			abstractVisitorVisit.param(beanClazz, parameterName);
			abstractVisitorVisit.body()._return(continueAction);

			JMethod abstractSimpleVisitorVisit = abstractSimpleVisitorClazz.method(JMod.PUBLIC, visitorActionClazz, "visit");
			abstractSimpleVisitorVisit.annotate(Override.class);
			abstractSimpleVisitorVisit.param(beanClazz, parameterName);
			abstractSimpleVisitorVisit.body()._return(JExpr.invoke(abstractSimpleVisitorDefaultVisit).arg(JExpr.cast(pmmlObjectClazz, JExpr.ref(parameterName))));

			JMethod beanAccept = beanClazz.method(JMod.PUBLIC, visitorActionClazz, "accept");
			beanAccept.annotate(Override.class);

			JVar visitorParameter = beanAccept.param(visitorInterface, "visitor");

			JBlock body = beanAccept.body();

			JVar status = body.decl(visitorActionClazz, "status", JExpr.invoke(visitorParameter, "visit").arg(JExpr._this()));

			int pushPos = body.pos();

			JInvocation traverseVarargs = null;

			FieldOutline[] fields = clazz.getDeclaredFields();
			for(FieldOutline field : fields){
				CPropertyInfo propertyInfo = field.getPropertyInfo();

				JType fieldType = field.getRawType();

				JMethod getterMethod = beanClazz.getMethod("get" + propertyInfo.getName(true), new JType[0]);

				// Collection of values
				if(propertyInfo.isCollection()){
					JType fieldElementType = CodeModelUtil.getElementType(fieldType);

					if(traversableTypes.contains(fieldElementType.name()) || objectClazz.equals(fieldElementType)){
						JMethod hasElementsMethod = beanClazz.getMethod("has" + propertyInfo.getName(true), new JType[0]);

						body._if((status.eq(continueAction)).cand(JExpr.invoke(hasElementsMethod)))._then().assign(status, pmmlObjectClazz.staticInvoke(traversableTypes.contains(fieldElementType.name()) ? "traverse" : "traverseMixed").arg(visitorParameter).arg(JExpr.invoke(getterMethod)));

						traverseVarargs = null;
					}
				} else

				// Simple value
				{
					if(traversableTypes.contains(fieldType.name())){

						if(traverseVarargs == null){
							traverseVarargs = pmmlObjectClazz.staticInvoke("traverse").arg(visitorParameter);

							body._if(status.eq(continueAction))._then().assign(status, traverseVarargs);
						}

						traverseVarargs.arg(JExpr.invoke(getterMethod));
					}
				}
			}

			int popPos = body.pos();

			body._if(status.eq(terminateAction))._then()._return(terminateAction);

			body._return(continueAction);

			if(pushPos < popPos){
				body.pos(popPos);
				body.add(JExpr.invoke(visitorParameter, visitorPopParent));

				body.pos(pushPos);
				body.add(JExpr.invoke(visitorParameter, visitorPushParent).arg(JExpr._this()));
			}
		}

		return true;
	}
}