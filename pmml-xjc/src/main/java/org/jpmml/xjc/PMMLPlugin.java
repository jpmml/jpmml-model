/*
 * Copyright (c) 2009 University of Tartu
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.xml.namespace.QName;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
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
import com.sun.tools.xjc.model.CReferencePropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.glassfish.jaxb.core.api.impl.NameConverter;
import org.glassfish.jaxb.core.v2.model.core.WildcardMode;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

public class PMMLPlugin extends ComplexPlugin {

	@Override
	public String getOptionName(){
		return "Xpmml";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	public List<QName> getCustomizationElementNames(){
		return Arrays.asList(PMMLPlugin.SERIALVERSIONUID_ELEMENT_NAME, PMMLPlugin.SUBPACKAGE_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		JCodeModel codeModel = model.codeModel;

		JClass measureClass = codeModel.ref("org.dmg.pmml.Measure");
		JClass nodeClass = codeModel.ref("org.dmg.pmml.tree.Node");
		JClass pmmlObjectClass = codeModel.ref("org.dmg.pmml.PMMLObject");
		JClass scoreDistributionClass = codeModel.ref("org.dmg.pmml.ScoreDistribution");

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
			CPluginCustomization serialVersionUIDCustomization = CustomizationUtil.findCustomization(model, PMMLPlugin.SERIALVERSIONUID_ELEMENT_NAME);

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
			CPluginCustomization subpackageCustomization = CustomizationUtil.findCustomization(classInfo, PMMLPlugin.SUBPACKAGE_ELEMENT_NAME);

			if(subpackageCustomization != null){
				CClassInfoParent.Package packageParent = (CClassInfoParent.Package)classInfo.parent();

				Element element = subpackageCustomization.element;

				String name = element.getAttribute("name");
				if(name == null){
					throw new RuntimeException();
				}

				try {
					Field pkgField = CClassInfoParent.Package.class.getDeclaredField("pkg");
					ensureAccessible(pkgField);

					JPackage subPackage = packageParent.pkg.subPackage(name);

					pkgField.set(packageParent, subPackage);
				} catch(ReflectiveOperationException roe){
					throw new RuntimeException(roe);
				}
			} // End if

			if((classInfo.shortName).equals("ComplexNode") || (classInfo.shortName).equals("ComplexScoreDistribution")){
				String name = classInfo.shortName.replace("Complex", "");

				try {
					Field elementNameField = CClassInfo.class.getDeclaredField("elementName");
					ensureAccessible(elementNameField);

					elementNameField.set(classInfo, new QName("https://www.dmg.org/PMML-4_4", name));
				} catch(ReflectiveOperationException roe){
					throw new RuntimeException(roe);
				}
			}

			List<CPropertyInfo> propertyInfos = classInfo.getProperties();
			propertyInfos.sort(comparator);

			for(CPropertyInfo propertyInfo : propertyInfos){
				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				if(propertyInfo instanceof CReferencePropertyInfo){
					CReferencePropertyInfo referencePropertyInfo = (CReferencePropertyInfo)propertyInfo;

					referencePropertyInfo.setWildcard(WildcardMode.LAX);
				} // End if

				// Collection of values
				if(propertyInfo.isCollection()){

					if((classInfo.shortName).equals("ComplexNode") && (privateName).equals("node")){
						propertyInfo.baseType = nodeClass;
					} else

					if(((classInfo.shortName).equals("ComplexNode") || (classInfo.shortName).equals("RuleSet") || (classInfo.shortName).equals("SimpleRule")) && (privateName).equals("scoreDistribution")){
						propertyInfo.baseType = scoreDistributionClass;
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

					if((classInfo.shortName).equals("DecisionTree") && (privateName).equals("missingValueStrategy")){
						propertyInfo.baseType = codeModel.directClass("org.dmg.pmml.tree.TreeModel.MissingValueStrategy");
						propertyInfo.defaultValue = new CEnumConstantDefaultValue(propertyInfo, propertyInfo.defaultValue);
					} else

					if((classInfo.shortName).equals("DecisionTree") && (privateName).equals("node")){
						propertyInfo.baseType = nodeClass;
					} else

					if((classInfo.shortName).equals("DecisionTree") && (privateName).equals("noTrueChildStrategy")){
						propertyInfo.baseType = codeModel.directClass("org.dmg.pmml.tree.TreeModel.NoTrueChildStrategy");
						propertyInfo.defaultValue = new CEnumConstantDefaultValue(propertyInfo, propertyInfo.defaultValue);
					} else

					if((classInfo.shortName).equals("DecisionTree") && (privateName).equals("splitCharacteristic")){
						propertyInfo.baseType = codeModel.directClass("org.dmg.pmml.tree.TreeModel.SplitCharacteristic");
						propertyInfo.defaultValue = new CEnumConstantDefaultValue(propertyInfo, propertyInfo.defaultValue);
					} else

					if((classInfo.shortName).equals("NeuralLayer") && (privateName).equals("activationFunction")){
						propertyInfo.baseType = codeModel.directClass("org.dmg.pmml.neural_network.NeuralNetwork.ActivationFunction");
					} else

					if((classInfo.shortName).equals("NeuralLayer") && (privateName).equals("normalizationMethod")){
						propertyInfo.baseType = codeModel.directClass("org.dmg.pmml.neural_network.NeuralNetwork.NormalizationMethod");
					} else

					if((classInfo.shortName).equals("Regression") && (privateName).equals("normalizationMethod")){
						propertyInfo.baseType = codeModel.directClass("org.dmg.pmml.regression.RegressionModel.NormalizationMethod");
						propertyInfo.defaultValue = new CEnumConstantDefaultValue(propertyInfo, propertyInfo.defaultValue);
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

		JClass missingAttributeExceptionClass = codeModel.ref("org.jpmml.model.MissingAttributeException");
		JClass missingElementExceptionClass = codeModel.ref("org.jpmml.model.MissingElementException");

		JClass propertyAnnotation = codeModel.ref("org.jpmml.model.annotations.Property");

		List<? extends ClassOutline> classOutlines = new ArrayList<>(outline.getClasses());
		classOutlines.sort((left, right) -> (left.implClass.name()).compareToIgnoreCase(right.implClass.name()));

		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			List<JAnnotationUse> beanClazzAnnotations = getAnnotations(beanClazz);

			JAnnotationUse xmlAccessorType = findAnnotation(beanClazzAnnotations, XmlAccessorType.class);
			if(xmlAccessorType != null){
				beanClazzAnnotations.remove(xmlAccessorType);
			}

			JAnnotationUse xmlRootElement = findAnnotation(beanClazzAnnotations, XmlRootElement.class);
			if(xmlRootElement != null){
				beanClazzAnnotations.remove(xmlRootElement);
				beanClazzAnnotations.add(0, xmlRootElement);
			}

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

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

					hasElementsMethod.body()._return((fieldRef.ne(JExpr._null())).cand((fieldRef.invoke("isEmpty")).not()));

					moveBefore(beanClazz, hasElementsMethod, getElementsMethod);

					JMethod addElementsMethod = beanClazz.method(JMod.PUBLIC, beanClazz, "add" + publicName);

					JVar param = addElementsMethod.varParam(elementType, name);

					addElementsMethod.body().add(JExpr.invoke(getterMethod).invoke("addAll").arg(arraysClass.staticInvoke("asList").arg(param)));
					addElementsMethod.body()._return(JExpr._this());

					moveAfter(beanClazz, addElementsMethod, getElementsMethod);
				}

				boolean required = OutlineUtil.isRequired(beanClazz, propertyInfo);

				List<CPluginCustomization> propertyCustomizations = CustomizationUtil.findPropertyCustomizationsInProperty(propertyInfo, AnnotatePlugin.ANNOTATE_PROPERTY_QNAME);
				for(CPluginCustomization propertyCustomization : propertyCustomizations){
					String[] classAndValue = AnnotatePlugin.parseCustomization(propertyCustomization);

					switch(classAndValue[0]){
						case "org.jpmml.model.annotations.NullSafeGetter":
							required |= true;
							break;
						default:
							break;
					}
				}

				if(propertyInfo instanceof CAttributePropertyInfo){
					CAttributePropertyInfo attributePropertyInfo = (CAttributePropertyInfo)propertyInfo;

					JFieldVar attributeVar = declareAttributeField(beanClazz, fieldVar);

					if(required){
						JFieldRef fieldRef = JExpr.refthis(fieldVar.name());

						JMethod requireMethod = beanClazz.method(JMod.PUBLIC, getterMethod.type(), "require" + publicName);

						requireMethod.body()._if(fieldRef.eq(JExpr._null()))._then()._throw(JExpr._new(missingAttributeExceptionClass).arg(JExpr._this()).arg(constantExpr(attributeVar)));
						requireMethod.body()._return(fieldRef);

						moveBefore(beanClazz, requireMethod, getterMethod);
					}
				} else

				if(propertyInfo instanceof CElementPropertyInfo){
					CElementPropertyInfo elementPropertyInfo = (CElementPropertyInfo)propertyInfo;

					JFieldVar elementVar = declareElementField(beanClazz, fieldVar);

					if(required){
						JFieldRef fieldRef = JExpr.refthis(fieldVar.name());

						JMethod requireMethod = beanClazz.method(JMod.PUBLIC, getterMethod.type(), "require" + publicName);

						JExpression testExpr = fieldRef.eq(JExpr._null());
						if(elementPropertyInfo.isCollection()){
							testExpr = testExpr.cor(fieldRef.invoke("isEmpty"));
						}

						requireMethod.body()._if(testExpr)._then()._throw(JExpr._new(missingElementExceptionClass).arg(JExpr._this()).arg(constantExpr(elementVar)));
						requireMethod.body()._return(fieldRef);

						moveBefore(beanClazz, requireMethod, getterMethod);
					}
				}

				List<JAnnotationUse> fieldVarAnnotations = getAnnotations(fieldVar);

				// XXX
				if(("node").equals(name) || ("nodes").equals(name) || ("scoreDistributions").equals(name)){
					JAnnotationUse xmlElement = findAnnotation(fieldVarAnnotations, XmlElement.class);

					fieldVarAnnotations.remove(xmlElement);

					JAnnotationUse xmlElements = fieldVar.annotate(XmlElements.class);

					JAnnotationArrayMember valueArray = xmlElements.paramArray("value");

					JAnnotationUse valueXmlElement = valueArray.annotate(XmlElement.class);

					addValues(valueXmlElement, xmlElement.getAnnotationMembers());

					fieldVarAnnotations.remove(xmlElements);
					fieldVarAnnotations.add(0, xmlElements);
				} // End if

				if(hasAnnotation(fieldVarAnnotations, XmlValue.class)){
					fieldVar.annotate(XmlValueExtension.class);
				}
			}

			FieldOutline contentFieldOutline = getContentField(classOutline);
			if(contentFieldOutline != null){
				CPropertyInfo propertyInfo = contentFieldOutline.getPropertyInfo();

				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				JFieldVar fieldVar = fieldVars.get(privateName);

				JType elementType = CodeModelUtil.getElementType(fieldVar.type());

				beanClazz._implements(iterableInterface.narrow(elementType));

				JMethod hasElementsMethod = beanClazz.getMethod("has" + publicName, new JType[0]);
				JMethod requireElementsMethod = beanClazz.getMethod("require" + publicName, new JType[0]);

				JMethod iteratorMethod = beanClazz.method(JMod.PUBLIC, iteratorInterface.narrow(elementType), "iterator");
				iteratorMethod.annotate(Override.class);

				iteratorMethod.body()._return(JExpr.invoke(requireElementsMethod).invoke("iterator"));

				moveBefore(beanClazz, iteratorMethod, hasElementsMethod);
			}

			FieldOutline extensionsFieldOutline = getExtensionsField(classOutline);
			if(extensionsFieldOutline != null){
				beanClazz._implements(hasExtensionsInterface.narrow(beanClazz));
			} // End if

			// Implementations of org.dmg.pmml.HasFieldReference
			if(checkType(beanClazz, "org.dmg.pmml.TextIndex")){
				createGetterProxy(beanClazz, stringClass, "getField", "getTextField");
				createGetterProxy(beanClazz, stringClass, "requireField", "requireTextField");
				createSetterProxy(beanClazz, stringClass, "field", "setField", "setTextField");
			} else

			if(checkType(beanClazz, "org.dmg.pmml.MiningField")){
				createGetterProxy(beanClazz, stringClass, "getField", "getName");
				createGetterProxy(beanClazz, stringClass, "requireField", "requireName");
				createSetterProxy(beanClazz, stringClass, "field", "setField", "setName");
			} // End if

			// Implementations of org.dmg.pmml.HasTargetFieldReference
			if(checkType(beanClazz, "org.dmg.pmml.Target")){
				createGetterProxy(beanClazz, stringClass, "getTargetField", "getField");
				createSetterProxy(beanClazz, stringClass, "targetField", "setTargetField", "setField");
			} // End if

			// Implementations of org.dmg.pmml.Indexable
			if(checkType(beanClazz, "org.dmg.pmml.MiningField")){
				createGetterProxy(beanClazz, stringClass, "getKey", "requireName");
			} else

			if(checkType(beanClazz, "org.dmg.pmml.VerificationField") || checkType(beanClazz, "org.dmg.pmml.nearest_neighbor.InstanceField")){
				createGetterProxy(beanClazz, stringClass, "getKey", "requireField");
			} else

			if(checkType(beanClazz, "org.dmg.pmml.Target")){
				createGetterProxy(beanClazz, stringClass, "getKey", "getTargetField");
			} // End if

			if(checkType(beanClazz, "org.dmg.pmml.False") || checkType(beanClazz, "org.dmg.pmml.True")){
				createSingleton(codeModel, beanClazz);
			} // End if

			if(model.serialVersionUID != null){
				beanClazz.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, long.class, "serialVersionUID", JExpr.lit(model.serialVersionUID));
			}

			String[][][] baseClassInfos = {
				{{"ComparisonField"}, {"getCompareFunction", "setCompareFunction", "getFieldWeight", "setFieldWeight", "getSimilarityScale", "setSimilarityScale"}},
				{{"EmbeddedModel"}, {"getAlgorithmName", "setAlgorithmName", "getLocalTransformations", "setLocalTransformations", "getMiningFunction", "requireMiningFunction", "setMiningFunction", "getModelName", "setModelName", "getModelStats", "setModelStats", "getOutput", "setOutput", "getTargets", "setTargets"}},
				{{"Field"}, {"requireName"}},
				{{"Kernel"}, {"getDescription", "setDescription"}},
				{{"Model"}, {"getAlgorithmName", "setAlgorithmName", "getLocalTransformations", "setLocalTransformations", "getMathContext", "setMathContext", "getMiningFunction", "requireMiningFunction", "setMiningFunction", "getMiningSchema", "requireMiningSchema", "setMiningSchema", "getModelExplanation", "setModelExplanation", "getModelName", "setModelName", "getModelStats", "setModelStats", "getModelVerification", "setModelVerification", "getOutput", "setOutput", "isScorable", "setScorable", "getTargets", "setTargets"}},
				{{"ModelQuality"}, {"getDataName", "setDataName"}},
				{{"NeuralEntity"}, {"requireId"}},
				{{"Node"}, {"requireDefaultChild"}},
				{{"ParameterCell"}, {"getParameterName", "requireParameterName", "setParameterName", "getTargetCategory", "setTargetCategory"}},
				{{"PredictorList"}, {"hasPredictors", "getPredictors", "addPredictors"}},
				{{"ScoreDistribution"}, {"getConfidence", "setConfidence", "getProbability", "requireProbability", "setProbability", "requireRecordCount"}},
				{{"SparseArray"}, {"getDefaultValue", "setDefaultValue", "hasEntries", "getEntries", "addEntries", "hasIndices", "getIndices", "addIndices", "getN", "setN"}},
				{{"Term"}, {"getCoefficient", "requireCoefficient", "setCoefficient"}}
			};

			for(String[][] baseClassInfo : baseClassInfos){
				addOverrideAnnotations(beanClazz, baseClassInfo);
			}

			String[][][] markerInterfaceInfos = {
				{{"HasActivationFunction"}, {"getActivationFunction", "setActivationFunction", "getAltitude", "setAltitude", "getLeakage", "setLeakage", "getThreshold", "setThreshold", "getWidth", "setWidth"}},
				{{"HasBaselineScore"}, {"getBaselineScore", "setBaselineScore"}},
				{{"HasContinuousDomain"}, {"hasIntervals", "getIntervals", "addIntervals"}},
				{{"HasDataType", "Field"}, {"getDataType", "setDataType"}},
				{{"HasDefaultValue"}, {"getDefaultValue", "setDefaultValue"}},
				{{"HasDerivedFields"}, {"hasDerivedFields", "getDerivedFields", "addDerivedFields"}},
				{{"HasDiscreteDomain"}, {"hasValues", "getValues", "addValues"}},
				{{"HasDisplayName", "Field"}, {"getDisplayName", "setDisplayName"}},
				{{"HasExpression"}, {"getExpression", "requireExpression", "setExpression"}},
				{{"HasExtensions"}, {"hasExtensions", "getExtensions", "addExtensions"}},
				{{"HasFieldReference", "ComparisonField"}, {"getField", "requireField", "setField"}},
				{{"HasId", "Entity", "NeuralEntity", "Node", "Rule"}, {"getId", "setId"}},
				{{"HasLocator"}, {"getLocator", "setLocator"}},
				{{"HasMapMissingTo"}, {"getMapMissingTo", "setMapMissingTo"}},
				{{"HasMixedContent"}, {"hasContent", "getContent", "addContent"}},
				{{"HasModel"}, {"getModel", "requireModel", "setModel"}},
				{{"HasName", "Field"}, {"getName", "setName"}},
				{{"HasNode"}, {"getMissingValueStrategy", "setMissingValueStrategy", "getMissingValuePenalty", "setMissingValuePenalty", "getNoTrueChildStrategy", "setNoTrueChildStrategy", "getSplitCharacteristic", "setSplitCharacteristic", "getNode", "setNode"}},
				{{"HasNormalizationMethod"}, {"getNormalizationMethod", "setNormalizationMethod"}},
				{{"HasOpType", "Field"}, {"getOpType", "setOpType"}},
				{{"HasPredicate", "Node", "Rule"}, {"getPredicate", "requirePredicate", "setPredicate"}},
				{{"HasReasonCode"}, {"getReasonCode", "setReasonCode"}},
				{{"HasRecordCount", "Node", "ScoreDistribution"}, {"getRecordCount", "setRecordCount"}},
				{{"HasRegressionTables"}, {"getNormalizationMethod", "setNormalizationMethod", "hasRegressionTables", "getRegressionTables", "requireRegressionTables", "addRegressionTables"}},
				{{"HasRequiredDataType", "Field"}, {"getDataType", "requireDataType", "setDataType"}},
				{{"HasRequiredId"}, {"getId", "requireId", "setId"}},
				{{"HasRequiredName", "Field"}, {"getName", "requireName", "setName"}},
				{{"HasRequiredOpType", "Field"}, {"getOpType", "requireOpType", "setOpType"}},
				{{"HasRequiredType"}, {"getDataType", "requireDataType", "setDataType", "getOpType", "requireOpType", "setOpType"}},
				{{"HasScore", "Node", "Rule"}, {"getScore", "setScore"}},
				{{"HasScoreDistributions", "Node", "Rule"}, {"requireScore", "hasScoreDistributions", "getScoreDistributions", "addScoreDistributions"}},
				{{"HasTable"}, {"getTableLocator", "setTableLocator", "getInlineTable", "setInlineTable"}},
				{{"HasTargetFieldReference"}, {"getTargetField", "setTargetField"}},
				{{"HasType"}, {"getDataType", "setDataType", "getOpType", "setOpType"}},
				{{"HasValue", "ScoreDistribution"}, {"getValue", "requireValue", "setValue"}},
				{{"HasValueSet"}, {"getArray", "requireArray", "setArray"}}
			};

			for(String[][] markerInterfaceInfo : markerInterfaceInfos){
				addOverrideAnnotations(beanClazz, markerInterfaceInfo);
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

		return Arrays.stream(classOutline.getDeclaredFields())
			.filter(predicate)
			.findFirst().orElse(null);
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

		return Arrays.stream(classOutline.getDeclaredFields())
			.filter(predicate)
			.findFirst().orElse(null);
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

			Field annotationsField;

			while(true){

				try {
					annotationsField = clazz.getDeclaredField("annotations");

					break;
				} catch(NoSuchFieldException nsfe){
					clazz = clazz.getSuperclass();

					if(clazz == null){
						throw nsfe;
					}
				}
			}

			ensureAccessible(annotationsField);

			return (List)annotationsField.get(annotatable);
		} catch(ReflectiveOperationException roe){
			throw new RuntimeException(roe);
		}
	}

	static
	private void addValues(JAnnotationUse annotationUse, Map<String, JAnnotationValue> memberValues){

		try {
			Method addValueMethod = JAnnotationUse.class.getDeclaredMethod("addValue", String.class, JAnnotationValue.class);
			ensureAccessible(addValueMethod);

			Collection<Map.Entry<String, JAnnotationValue>> entries = memberValues.entrySet();
			for(Map.Entry<String, JAnnotationValue> entry : entries){
				addValueMethod.invoke(annotationUse, entry.getKey(), entry.getValue());
			}
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
	private JFieldVar declareAttributeField(JDefinedClass beanClazz, JFieldVar fieldVar){
		JDefinedClass attributesInterface = ensureInterface(beanClazz._package(), "PMMLAttributes");

		return declareField(attributesInterface, beanClazz, fieldVar);
	}

	static
	private JFieldVar declareElementField(JDefinedClass beanClazz, JFieldVar fieldVar){
		JDefinedClass elementsInterface = ensureInterface(beanClazz._package(), "PMMLElements");

		return declareField(elementsInterface, beanClazz, fieldVar);
	}

	static
	private JFieldVar declareField(JDefinedClass _interface, JDefinedClass beanClazz, JFieldVar fieldVar){
		JCodeModel codeModel = _interface.owner();

		JExpression initExpr = codeModel.ref("org.jpmml.model.ReflectionUtil").staticInvoke("getField").arg(beanClazz.dotclass()).arg(fieldVar.name());

		return _interface.field(0, Field.class, (beanClazz.name() + "_" + fieldVar.name()).toUpperCase(), initExpr);
	}

	static
	private JExpression constantExpr(JFieldVar fieldVar){
		JDefinedClass owner;

		try {
			Field ownerField = JFieldVar.class.getDeclaredField("owner");
			ensureAccessible(ownerField);

			owner = (JDefinedClass)ownerField.get(fieldVar);
		} catch(ReflectiveOperationException roe){
			throw new RuntimeException(roe);
		}

		return owner.staticRef(fieldVar.name());
	}

	static
	private void addOverrideAnnotations(JDefinedClass beanClazz, String[][] typeMemberInfo){
		Set<String> typeNames = new LinkedHashSet<>(Arrays.asList(typeMemberInfo[0]));
		Set<String> methodNames = new LinkedHashSet<>(Arrays.asList(typeMemberInfo[1]));

		boolean matches = false;

		{
			JClass beanSuperClazz = beanClazz._extends();

			String name = (beanSuperClazz.erasure()).name();

			matches |= typeNames.contains(name);
		}

		for(Iterator<JClass> it = beanClazz._implements(); it.hasNext(); ){
			JClass _interface = it.next();

			String name = (_interface.erasure()).name();

			matches |= typeNames.contains(name);
		}

		if(!matches){
			return;
		}

		Collection<JMethod> methods = beanClazz.methods();

		for(JMethod method : methods){
			String name = method.name();

			if(!methodNames.contains(name)){
				continue;
			}

			List<JVar> params = method.params();

			if((name.startsWith("has") || name.startsWith("is") || name.startsWith("get") || name.startsWith("require")) && params.size() == 0){

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
			} else

			{
				throw new RuntimeException();
			}
		}
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

	@SuppressWarnings("deprecation")
	static
	private void ensureAccessible(AccessibleObject accessibleObject){

		if(!accessibleObject.isAccessible()){
			accessibleObject.setAccessible(true);
		}
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
	private class CEnumConstantDefaultValue extends CDefaultValue {

		private CPropertyInfo propertyInfo = null;

		private CDefaultValue parent = null;


		private CEnumConstantDefaultValue(CPropertyInfo propertyInfo, CDefaultValue parent){
			setPropertyInfo(propertyInfo);
			setParent(parent);
		}

		@Override
		public JExpression compute(Outline outline){
			CPropertyInfo propertyInfo = getPropertyInfo();
			CDefaultValue parent = getParent();

			JStringLiteral stringLiteral = (JStringLiteral)parent.compute(outline);

			String name = NameConverter.standard.toConstantName(stringLiteral.str);

			return ((JClass)propertyInfo.baseType).staticRef(name);
		}

		public CPropertyInfo getPropertyInfo(){
			return this.propertyInfo;
		}

		private void setPropertyInfo(CPropertyInfo propertyInfo){
			this.propertyInfo = propertyInfo;
		}

		public CDefaultValue getParent(){
			return this.parent;
		}

		private void setParent(CDefaultValue parent){
			this.parent = parent;
		}
	}

	static
	private class CShareableDefaultValue extends CDefaultValue {

		private CPropertyInfo propertyInfo = null;

		private CDefaultValue parent = null;

		private String field = null;


		private CShareableDefaultValue(CPropertyInfo propertyInfo, CDefaultValue parent){
			setPropertyInfo(propertyInfo);
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

		public CPropertyInfo getPropertyInfo(){
			return this.propertyInfo;
		}

		private void setPropertyInfo(CPropertyInfo propertyInfo){
			this.propertyInfo = propertyInfo;
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

	public static final String NAMESPACE_URI = "http://jpmml.org/jpmml-model";

	public static final QName SERIALVERSIONUID_ELEMENT_NAME = new QName(PMMLPlugin.NAMESPACE_URI, "serialVersionUID");
	public static final QName SUBPACKAGE_ELEMENT_NAME = new QName(PMMLPlugin.NAMESPACE_URI, "subpackage");
}
