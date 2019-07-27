/*
 * Copyright (c) 2009 University of Tartu
 */
package org.jpmml.xjc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JJavaName;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JStringLiteral;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CAttributePropertyInfo;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CClassInfoParent;
import com.sun.tools.xjc.model.CDefaultValue;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

public class PMMLPlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName(){
		return "Xpmml";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public Collection<QName> getCustomizationElementNames(){
		return Arrays.asList(PMMLPlugin.SERIALVERSIONUID_ELEMENT_NAME, PMMLPlugin.SUBPACKAGE_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		JCodeModel codeModel = model.codeModel;

		JClass measureClass = codeModel.ref("org.dmg.pmml.Measure");
		JClass nodeClass = codeModel.ref("org.dmg.pmml.tree.Node");
		JClass pmmlObjectClass = codeModel.ref("org.dmg.pmml.PMMLObject");

		JClass activationFunctionEnum = codeModel.directClass("org.dmg.pmml.neural_network.NeuralNetwork.ActivationFunction");
		JClass normalizationMethodEnum = codeModel.directClass("org.dmg.pmml.neural_network.NeuralNetwork.NormalizationMethod");

		Comparator<CPropertyInfo> comparator = new Comparator<CPropertyInfo>(){

			@Override
			public int compare(CPropertyInfo left, CPropertyInfo right){
				boolean leftAttribute = (left instanceof CAttributePropertyInfo);
				boolean rightAttribute = (right instanceof CAttributePropertyInfo);

				if(leftAttribute && !rightAttribute){
					return -1;
				} else

				if(!leftAttribute && rightAttribute){
					return 1;
				}

				return 0;
			}
		};

		{
			CPluginCustomization serialVersionUIDCustomization = CustomizationUtils.findCustomization(model, PMMLPlugin.SERIALVERSIONUID_ELEMENT_NAME);

			if(serialVersionUIDCustomization != null){
				Element element = serialVersionUIDCustomization.element;

				if(model.serialVersionUID != null){
					throw new RuntimeException();
				}

				int major = parseVersion(element.getAttribute("major"));
				int minor = parseVersion(element.getAttribute("minor"));
				int patch = parseVersion(element.getAttribute("patch"));

				int implementation = parseVersion(element.getAttribute("implementation"));

				model.serialVersionUID = (long)((major << 24) | (minor << 16) | (patch << 8) | implementation);
			}
		}

		Map<NClass, CClassInfo> beans = model.beans();

		Collection<CClassInfo> classInfos = beans.values();
		for(CClassInfo classInfo : classInfos){
			CPluginCustomization subpackageCustomization = CustomizationUtils.findCustomization(classInfo, PMMLPlugin.SUBPACKAGE_ELEMENT_NAME);

			if(subpackageCustomization != null){
				CClassInfoParent.Package packageParent = (CClassInfoParent.Package)classInfo.parent();

				Element element = subpackageCustomization.element;

				String name = element.getAttribute("name");
				if(name == null){
					throw new RuntimeException();
				}

				try {
					Field field = CClassInfoParent.Package.class.getDeclaredField("pkg");
					if(!field.isAccessible()){
						field.setAccessible(true);
					}

					JPackage subPackage = packageParent.pkg.subPackage(name);

					field.set(packageParent, subPackage);
				} catch(ReflectiveOperationException roe){
					throw new RuntimeException(roe);
				}
			} // End if

			if((classInfo.shortName).equals("ComplexNode")){

				try {
					Field field = CClassInfo.class.getDeclaredField("elementName");
					if(!field.isAccessible()){
						field.setAccessible(true);
					}

					field.set(classInfo, new QName("http://www.dmg.org/PMML-4_3", "Node"));
				} catch(ReflectiveOperationException roe){
					throw new RuntimeException(roe);
				}
			}

			List<CPropertyInfo> propertyInfos = classInfo.getProperties();
			propertyInfos.sort(comparator);

			for(CPropertyInfo propertyInfo : propertyInfos){
				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				// Collection of values
				if(propertyInfo.isCollection()){

					if((classInfo.shortName).equals("ComplexNode") && (privateName).equals("node")){
						propertyInfo.baseType = nodeClass;
					} else

					if((classInfo.shortName).equals("VectorFields") && (privateName).equals("fieldRefOrCategoricalPredictor")){
						propertyInfo.baseType = pmmlObjectClass;
					} // End if

					if((privateName).contains("And") || (privateName).contains("Or") || (privateName).equalsIgnoreCase("content")){
						propertyInfo.setName(true, "Content");
						propertyInfo.setName(false, "content");
					} else

					{
						// Have "arrays" instead of "arraies"
						if((privateName).endsWith("array") || (privateName).endsWith("Array")){
							publicName += "s";
							privateName += "s";
						} else

						// Have "refs" instead of "reves"
						if((privateName).endsWith("ref") || (privateName).endsWith("Ref")){
							publicName += "s";
							privateName += "s";
						} else

						{
							publicName = JJavaName.getPluralForm(publicName);
							privateName = JJavaName.getPluralForm(privateName);
						}

						propertyInfo.setName(true, publicName);
						propertyInfo.setName(false, privateName);
					}
				} else

				// Simple value
				{
					if((classInfo.shortName).equals("ComparisonMeasure") && (privateName).equals("measure")){
						propertyInfo.baseType = measureClass;
					} else

					if((classInfo.shortName).equals("DecisionTree") && (privateName).equals("node")){
						propertyInfo.baseType = nodeClass;
					} else

					if((classInfo.shortName).equals("NeuralLayer") && (privateName).equals("activationFunction")){
						propertyInfo.baseType = activationFunctionEnum;
					} else

					if((classInfo.shortName).equals("NeuralLayer") && (privateName).equals("normalizationMethod")){
						propertyInfo.baseType = normalizationMethodEnum;
					} else

					if((classInfo.shortName).equals("TreeModel") && (privateName).equals("node")){
						propertyInfo.baseType = nodeClass;
					} // End if

					if((privateName).equals("functionName")){
						propertyInfo.setName(true, "MiningFunction");
						propertyInfo.setName(false, "miningFunction");
					}

					CDefaultValue defaultValue = propertyInfo.defaultValue;
					if(defaultValue != null){
						propertyInfo.defaultValue = new CShareableDefaultValue(propertyInfo, propertyInfo.defaultValue);
					}
				}
			}
		}
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		Model model = outline.getModel();

		JCodeModel codeModel = model.codeModel;

		JClass iterableInterface = codeModel.ref("java.lang.Iterable");
		JClass iteratorInterface = codeModel.ref("java.util.Iterator");

		JClass hasExtensionsInterface = codeModel.ref("org.dmg.pmml.HasExtensions");
		JClass stringValueInterface = codeModel.ref("org.dmg.pmml.StringValue");

		JClass stringClass = codeModel.ref("java.lang.String");
		JClass arraysClass = codeModel.ref("java.util.Arrays");

		JClass fieldNameClass = codeModel.ref("org.dmg.pmml.FieldName");

		JClass propertyAnnotation = codeModel.ref("org.jpmml.model.annotations.Property");

		List<? extends ClassOutline> classOutlines = new ArrayList<>(outline.getClasses());
		classOutlines.sort((left, right) -> (left.implClass.name()).compareToIgnoreCase(right.implClass.name()));

		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			// Implementations of org.dmg.pmml.HasFieldReference
			if(checkType(beanClazz, "org.dmg.pmml.TextIndex")){
				createGetterProxy(beanClazz, fieldNameClass, "getField", "getTextField");
				createSetterProxy(beanClazz, fieldNameClass, "field", "setField", "setTextField");
			} // End if

			// Implementations of org.dmg.pmml.HasName
			if(checkType(beanClazz, "org.dmg.pmml.regression.CategoricalPredictor") || checkType(beanClazz, "org.dmg.pmml.regression.NumericPredictor")){
				createGetterProxy(beanClazz, fieldNameClass, "getName", "getField");
				createSetterProxy(beanClazz, fieldNameClass, "name", "setName", "setField");
			} // End if

			// Implementations of org.dmg.pmml.Indexable
			if(checkType(beanClazz, "org.dmg.pmml.DefineFunction") || checkType(beanClazz, "org.dmg.pmml.general_regression.Parameter")){
				createGetterProxy(beanClazz, stringClass, "getKey", "getName");
			} else

			if(checkType(beanClazz, "org.dmg.pmml.MiningField")){
				createGetterProxy(beanClazz, fieldNameClass, "getKey", "getName");
			} else

			if(checkType(beanClazz, "org.dmg.pmml.Target") || checkType(beanClazz, "org.dmg.pmml.VerificationField") || checkType(beanClazz, "org.dmg.pmml.nearest_neighbor.InstanceField")){
				createGetterProxy(beanClazz, fieldNameClass, "getKey", "getField");
			} else

			if(checkType(beanClazz, "org.dmg.pmml.association.Item") || checkType(beanClazz, "org.dmg.pmml.association.Itemset") || checkType(beanClazz, "org.dmg.pmml.sequence.Sequence") || checkType(beanClazz, "org.dmg.pmml.support_vector_machine.VectorInstance") || checkType(beanClazz, "org.dmg.pmml.text.TextDocument")){
				createGetterProxy(beanClazz, stringClass, "getKey", "getId");
			}

			List<JAnnotationUse> beanClazzAnnotations = getAnnotations(beanClazz);

			JAnnotationUse xmlAccessorType = findAnnotation(beanClazzAnnotations, XmlAccessorType.class);
			if(xmlAccessorType != null){
				beanClazzAnnotations.remove(xmlAccessorType);
			}

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			FieldOutline contentFieldOutline = getContentField(classOutline);
			if(contentFieldOutline != null){
				CPropertyInfo propertyInfo = contentFieldOutline.getPropertyInfo();

				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				JFieldVar fieldVar = fieldVars.get(privateName);

				JType elementType = CodeModelUtil.getElementType(fieldVar.type());

				beanClazz._implements(iterableInterface.narrow(elementType));

				JMethod getElementsMethod = beanClazz.getMethod("get" + publicName, new JType[0]);

				JMethod iteratorMethod = beanClazz.method(JMod.PUBLIC, iteratorInterface.narrow(elementType), "iterator");
				iteratorMethod.annotate(Override.class);

				iteratorMethod.body()._return(JExpr.invoke(getElementsMethod).invoke("iterator"));

				moveBefore(beanClazz, iteratorMethod, getElementsMethod);
			}

			FieldOutline extensionsFieldOutline = getExtensionsField(classOutline);
			if(extensionsFieldOutline != null){
				beanClazz._implements(hasExtensionsInterface.narrow(beanClazz));
			}

			FieldOutline[] fieldOutlines = classOutline.getDeclaredFields();
			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				JFieldVar fieldVar = fieldVars.get(privateName);

				String name = fieldVar.name();

				JMods modifiers = fieldVar.mods();
				if((modifiers.getValue() & JMod.PRIVATE) != JMod.PRIVATE){
					modifiers.setPrivate();
				}

				JType type = fieldVar.type();

				CShareableDefaultValue defaultValue = (CShareableDefaultValue)propertyInfo.defaultValue;
				if(defaultValue != null){

					if(defaultValue.isShared()){
						beanClazz.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, type, defaultValue.getField(), defaultValue.computeInit(outline));
					}
				}

				JMethod getterMethod = beanClazz.getMethod("get" + publicName, new JType[0]);
				JMethod setterMethod = beanClazz.getMethod("set" + publicName, new JType[]{type});

				if(getterMethod != null){
					JType returnType = getterMethod.type();

					if(returnType.isPrimitive() && !type.isPrimitive()){
						JType boxifiedReturnType = returnType.boxify();

						if((boxifiedReturnType).equals(type)){
							getterMethod.type(boxifiedReturnType);
						}
					}
				} // End if

				if(setterMethod != null){
					setterMethod.type(beanClazz);

					JVar param = (setterMethod.params()).get(0);

					param.name(name);

					param.annotate(propertyAnnotation).param("value", name);

					setterMethod.body()._return(JExpr._this());
				} // End if

				if(propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(type);

					JFieldRef fieldRef = JExpr.refthis(name);

					JMethod getElementsMethod = beanClazz.getMethod("get" + publicName, new JType[0]);

					JMethod hasElementsMethod = beanClazz.method(JMod.PUBLIC, boolean.class, "has" + publicName);

					hasElementsMethod.body()._return((fieldRef.ne(JExpr._null())).cand((fieldRef.invoke("size")).gt(JExpr.lit(0))));

					moveBefore(beanClazz, hasElementsMethod, getElementsMethod);

					JMethod addElementsMethod = beanClazz.method(JMod.PUBLIC, beanClazz, "add" + publicName);

					JVar param = addElementsMethod.varParam(elementType, name);

					addElementsMethod.body().add(JExpr.invoke(getterMethod).invoke("addAll").arg(arraysClass.staticInvoke("asList").arg(param)));
					addElementsMethod.body()._return(JExpr._this());

					moveAfter(beanClazz, addElementsMethod, getElementsMethod);
				} // End if

				if(propertyInfo instanceof CAttributePropertyInfo){
					declareAttributeField(beanClazz, fieldVar);
				} else

				if(propertyInfo instanceof CElementPropertyInfo){
					declareElementField(beanClazz, fieldVar);
				}

				List<JAnnotationUse> fieldVarAnnotations = getAnnotations(fieldVar);

				if(hasAnnotation(fieldVarAnnotations, XmlValue.class)){
					fieldVar.annotate(XmlValueExtension.class);
				}
			}

			if(checkType(beanClazz, "org.dmg.pmml.False") || checkType(beanClazz, "org.dmg.pmml.True")){
				createSingleton(codeModel, beanClazz);
			} // End if

			if(model.serialVersionUID != null){
				beanClazz.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, long.class, "serialVersionUID", JExpr.lit(model.serialVersionUID));
			}

			String[][][] markerInterfaces = {
				{{"HasContinuousDomain"}, {"hasIntervals", "getIntervals", "addIntervals"}},
				{{"HasDataType", "Field"}, {"getDataType", "setDataType"}},
				{{"HasDefaultValue"}, {"getDefaultValue", "setDefaultValue"}},
				{{"HasDerivedFields"}, {"hasDerivedFields", "getDerivedFields", "addDerivedFields"}},
				{{"HasDiscreteDomain"}, {"hasValues", "getValues", "addValues"}},
				{{"HasDisplayName"}, {"getDisplayName", "setDisplayName"}},
				{{"HasExpression"}, {"getExpression", "setExpression"}},
				{{"HasExtensions"}, {"hasExtensions", "getExtensions", "addExtensions"}},
				{{"HasFieldReference", "ComparisonField"}, {"getField", "setField"}},
				{{"HasId", "Entity", "NeuralEntity", "Node", "Rule"}, {"getId", "setId"}},
				{{"HasLocator"}, {"getLocator", "setLocator"}},
				{{"HasMapMissingTo"}, {"getMapMissingTo", "setMapMissingTo"}},
				{{"HasMixedContent"}, {"hasContext", "getContent", "addContent"}},
				{{"HasName", "Field", "Term"}, {"getName", "setName"}},
				{{"HasOpType", "Field"}, {"getOpType", "setOpType"}},
				{{"HasPredicate", "Node", "Rule"}, {"getPredicate", "setPredicate"}},
				{{"HasScore", "Node", "Rule"}, {"getScore", "setScore"}},
				{{"HasTable"}, {"getTableLocator", "setTableLocator", "getInlineTable", "setInlineTable"}},
				{{"HasValue"}, {"getValue", "setValue"}},
				{{"HasValueSet"}, {"getArray", "setArray"}}
			};

			for(String[][] markerInterface : markerInterfaces){
				String[] types = markerInterface[0];
				String[] members = markerInterface[1];

				boolean matches = false;

				{
					JClass superClazz = beanClazz._extends();

					superClazz = superClazz.erasure();

					for(int i = 1; i < types.length; i++){
						matches |= (superClazz.name()).equals(types[i]);
					}
				}

				for(Iterator<JClass> it = beanClazz._implements(); it.hasNext(); ){
					JClass _interface = it.next();

					_interface = _interface.erasure();

					matches |= (_interface.name()).equals(types[0]);
				}

				if(!matches){
					continue;
				}

				Collection<JMethod> methods = beanClazz.methods();

				for(int i = 0; i < members.length; i++){

					for(JMethod method : methods){
						String name = method.name();

						if(!(name).equals(members[i])){
							continue;
						} // End if

						List<JVar> params = method.params();

						if((name.startsWith("has") || name.startsWith("get")) && params.size() == 0){

							if(!hasAnnotation(method.annotations(), Override.class)){
								method.annotate(Override.class);
							}
						} else

						if(name.startsWith("add") && params.size() == 0 && method.hasVarArgs()){
							method.annotate(Override.class);
						} else

						if(name.startsWith("set") && params.size() == 1){

							if(!hasAnnotation(method.annotations(), Override.class)){
								method.annotate(Override.class);
							}
						}
					}
				}
			}
		}

		List<? extends EnumOutline> enumOutlines = new ArrayList<>(outline.getEnums());
		enumOutlines.sort((left, right) -> (left.clazz.name()).compareToIgnoreCase(right.clazz.name()));

		for(EnumOutline enumOutline : enumOutlines){
			JDefinedClass clazz = enumOutline.clazz;

			clazz._implements(stringValueInterface.narrow(clazz));

			JMethod valueMethod = clazz.getMethod("value", new JType[0]);
			valueMethod.annotate(Override.class);

			JMethod toStringMethod = clazz.method(JMod.PUBLIC, String.class, "toString");
			toStringMethod.annotate(Override.class);

			toStringMethod.body()._return(JExpr.invoke(valueMethod));
		}

		if(model.serialVersionUID != null){
			model.serialVersionUID = null;
		}

		return true;
	}

	static
	private FieldOutline getExtensionsField(ClassOutline classOutline){
		Predicate<FieldOutline> predicate = new Predicate<FieldOutline>(){

			@Override
			public boolean test(FieldOutline fieldOutline){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				if(("extensions").equals(propertyInfo.getName(false)) && propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(fieldOutline.getRawType());

					return checkType(elementType, "org.dmg.pmml.Extension");
				}

				return false;
			}
		};

		return XJCUtil.findSingletonField(classOutline.getDeclaredFields(), predicate);
	}

	static
	private FieldOutline getContentField(ClassOutline classOutline){
		Predicate<FieldOutline> predicate = new Predicate<FieldOutline>(){

			private String name = classOutline.implClass.name();


			@Override
			public boolean test(FieldOutline fieldOutline){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				if(propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(fieldOutline.getRawType());

					String name = elementType.name();

					return ((this.name).equals(name + "s") || (this.name).equals(JJavaName.getPluralForm(name)));
				}

				return false;
			}
		};

		return XJCUtil.findSingletonField(classOutline.getDeclaredFields(), predicate);
	}

	static
	private boolean hasAnnotation(Collection<JAnnotationUse> annotations, Class<?> clazz){
		JAnnotationUse annotation = findAnnotation(annotations, clazz);

		return (annotation != null);
	}

	static
	private JAnnotationUse findAnnotation(Collection<JAnnotationUse> annotations, Class<?> clazz){
		String fullName = clazz.getName();

		for(JAnnotationUse annotation : annotations){
			JClass type = annotation.getAnnotationClass();

			if(checkType(type, fullName)){
				return annotation;
			}
		}

		return null;
	}

	static
	private List<JAnnotationUse> getAnnotations(JAnnotatable annotatable){

		try {
			Class<?> clazz = annotatable.getClass();

			Field field;

			while(true){

				try {
					field = clazz.getDeclaredField("annotations");

					break;
				} catch(NoSuchFieldException nsfe){
					clazz = clazz.getSuperclass();

					if(clazz == null){
						throw nsfe;
					}
				}
			}

			if(!field.isAccessible()){
				field.setAccessible(true);
			}

			return (List)field.get(annotatable);
		} catch(ReflectiveOperationException roe){
			throw new RuntimeException(roe);
		}
	}

	static
	private void createGetterProxy(JDefinedClass beanClazz, JType type, String name, String getterName){
		JMethod getterMethod = beanClazz.getMethod(getterName, new JType[0]);

		JMethod method = beanClazz.method(JMod.PUBLIC, type, name);
		method.annotate(Override.class);

		method.body()._return(JExpr.invoke(getterMethod));

		moveBefore(beanClazz, method, getterMethod);
	}

	static
	public void createSetterProxy(JDefinedClass beanClazz, JType type, String parameterName, String name, String setterName){
		JMethod getterMethod = beanClazz.getMethod(setterName.replace("set", "get"), new JType[0]);

		JMethod method = beanClazz.method(JMod.PUBLIC, beanClazz, name);
		method.annotate(Override.class);

		JVar nameParameter = method.param(type, parameterName);

		method.body()._return(JExpr.invoke(setterName).arg(nameParameter));

		moveBefore(beanClazz, method, getterMethod);
	}

	static
	public void createSingleton(JCodeModel codeModel, JDefinedClass beanClazz){
		JDefinedClass definedBeanClazz = codeModel.anonymousClass(beanClazz);

		JMethod hasExtensionsMethod = definedBeanClazz.method(JMod.PUBLIC, boolean.class, "hasExtensions");
		hasExtensionsMethod.annotate(Override.class);

		(hasExtensionsMethod.body())._return(JExpr.FALSE);

		JMethod getExtensionsMethod = definedBeanClazz.method(JMod.PUBLIC, List.class, "getExtensions");
		getExtensionsMethod.annotate(Override.class);

		(getExtensionsMethod.body())._throw(JExpr._new(codeModel.ref(UnsupportedOperationException.class)));

		JFieldVar instanceField = beanClazz.field(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, beanClazz, "INSTANCE", JExpr._new(definedBeanClazz));
	}

	static
	private void declareAttributeField(JDefinedClass beanClazz, JFieldVar fieldVar){
		JDefinedClass attributesInterface = ensureInterface(beanClazz._package(), "PMMLAttributes");

		declareField(attributesInterface, beanClazz, fieldVar);
	}

	static
	private void declareElementField(JDefinedClass beanClazz, JFieldVar fieldVar){
		JDefinedClass elementsInterface = ensureInterface(beanClazz._package(), "PMMLElements");

		declareField(elementsInterface, beanClazz, fieldVar);
	}

	static
	private void declareField(JDefinedClass _interface, JDefinedClass beanClazz, JFieldVar fieldVar){
		JCodeModel codeModel = _interface.owner();

		JExpression init = codeModel.ref("org.jpmml.model.ReflectionUtil").staticInvoke("getField").arg(beanClazz.dotclass()).arg(fieldVar.name());

		_interface.field(0, Field.class, (beanClazz.name() + "_" + fieldVar.name()).toUpperCase(), init);
	}

	static
	private JDefinedClass ensureInterface(JPackage _package, String name){

		try {
			return _package._interface(name);
		} catch(JClassAlreadyExistsException jcaee){
			return jcaee.getExistingClass();
		}
	}

	static
	private void moveBefore(JDefinedClass beanClazz, JMethod method, JMethod referenceMethod){
		List<JMethod> methods = (List<JMethod>)beanClazz.methods();

		int index = methods.indexOf(referenceMethod);
		if(index < 0){
			throw new RuntimeException();
		}

		methods.remove(method);
		methods.add(index, method);
	}

	static
	private void moveAfter(JDefinedClass beanClazz, JMethod method, JMethod referenceMethod){
		List<JMethod> methods = (List<JMethod>)beanClazz.methods();

		int index = methods.indexOf(referenceMethod);
		if(index < 0){
			throw new RuntimeException();
		}

		methods.remove(method);
		methods.add(index + 1, method);
	}

	static
	private boolean checkType(JType type, String fullName){
		return (type.fullName()).equals(fullName);
	}

	static
	private int parseVersion(String version){

		if(version == null){
			throw new RuntimeException();
		}

		int value = Integer.parseInt(version);
		if(value < 0 || value > 255){
			throw new RuntimeException();
		}

		return value;
	}

	static
	private class CShareableDefaultValue extends CDefaultValue {

		private CDefaultValue parent = null;

		private String field = null;


		private CShareableDefaultValue(CPropertyInfo propertyInfo, CDefaultValue parent){
			setParent(parent);
			setField(formatField(propertyInfo.getName(false)));
		}

		@Override
		public JExpression compute(Outline outline){
			JExpression expression = computeInit(outline);

			if((expression instanceof JFieldRef) || (expression instanceof JStringLiteral)){
				setField(null);

				return expression;
			}

			return JExpr.ref(getField());
		}

		public JExpression computeInit(Outline outline){
			CDefaultValue parent = getParent();

			return parent.compute(outline);
		}

		public boolean isShared(){
			String field = getField();

			return (field != null);
		}

		public CDefaultValue getParent(){
			return this.parent;
		}

		private void setParent(CDefaultValue parent){
			this.parent = parent;
		}

		public String getField(){
			return this.field;
		}

		private void setField(String field){
			this.field = field;
		}

		static
		private String formatField(String string){
			StringBuilder sb = new StringBuilder();
			sb.append("DEFAULT_");

			for(int i = 0; i < string.length(); i++){
				char c = string.charAt(i);

				if(Character.isUpperCase(c)){
					sb.append('_');
				}

				sb.append(Character.toUpperCase(c));
			}

			return sb.toString();
		}
	}

	public static final QName SERIALVERSIONUID_ELEMENT_NAME = new QName("http://jpmml.org/jpmml-model", "serialVersionUID");

	public static final QName SUBPACKAGE_ELEMENT_NAME = new QName("http://jpmml.org/jpmml-model", "subpackage");
}
