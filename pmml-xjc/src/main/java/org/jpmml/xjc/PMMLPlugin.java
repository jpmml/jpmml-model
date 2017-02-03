/*
 * Copyright (c) 2009 University of Tartu
 */
package org.jpmml.xjc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JCommentPart;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
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
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.outline.ClassOutline;
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
		return Arrays.asList(PMMLPlugin.SUBPACKAGE_ELEMENT_NAME);
	}

	@Override
	public void postProcessModel(Model model, ErrorHandler errorHandler){
		super.postProcessModel(model, errorHandler);

		JCodeModel codeModel = model.codeModel;

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
					field.setAccessible(true);

					JPackage subPackage = packageParent.pkg.subPackage(name);

					field.set(packageParent, subPackage);
				} catch(ReflectiveOperationException roe){
					throw new RuntimeException(roe);
				}
			}

			List<CPropertyInfo> propertyInfos = classInfo.getProperties();
			Collections.sort(propertyInfos, comparator);

			for(CPropertyInfo propertyInfo : propertyInfos){
				String publicName = propertyInfo.getName(true);
				String privateName = propertyInfo.getName(false);

				// Collection of values
				if(propertyInfo.isCollection()){

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
					if((classInfo.shortName).equals("NeuralLayer") && (privateName).equals("activationFunction")){
						propertyInfo.baseType = activationFunctionEnum;
					} else

					if((classInfo.shortName).equals("NeuralLayer") && (privateName).equals("normalizationMethod")){
						propertyInfo.baseType = normalizationMethodEnum;
					} // End if

					if((privateName).equals("isScorable")){
						propertyInfo.setName(true, "Scorable");
						propertyInfo.setName(false, "scorable");
					} else

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

		JClass arraysClass = codeModel.ref("java.util.Arrays");

		JClass fieldNameClass = codeModel.ref("org.dmg.pmml.FieldName");

		Collection<? extends ClassOutline> clazzes = outline.getClasses();
		for(ClassOutline clazz : clazzes){
			JDefinedClass beanClazz = clazz.implClass;

			// Implementations of org.dmg.pmml.HasValue
			if(checkType(beanClazz, "org.dmg.pmml.general_regression.PPCell")){
				JMethod fieldMethod = beanClazz.method(JMod.PUBLIC, fieldNameClass, "getField");
				fieldMethod.annotate(Override.class);
				fieldMethod.body()._return(JExpr.invoke("getPredictorName"));
			} else

			if(checkType(beanClazz, "org.dmg.pmml.regression.CategoricalPredictor")){
				JMethod fieldMethod = beanClazz.method(JMod.PUBLIC, fieldNameClass, "getField");
				fieldMethod.annotate(Override.class);
				fieldMethod.body()._return(JExpr.invoke("getName"));
			} // End if

			// Implementations of org.dmg.pmml.Indexable
			if(checkType(beanClazz, "org.dmg.pmml.DefineFunction") || checkType(beanClazz, "org.dmg.pmml.general_regression.Parameter")){
				JMethod keyMethod = beanClazz.method(JMod.PUBLIC, String.class, "getKey");
				keyMethod.annotate(Override.class);
				keyMethod.body()._return(JExpr.invoke("getName"));
			} else

			if(checkType(beanClazz, "org.dmg.pmml.MiningField")){
				JMethod keyMethod = beanClazz.method(JMod.PUBLIC, fieldNameClass, "getKey");
				keyMethod.annotate(Override.class);
				keyMethod.body()._return(JExpr.invoke("getName"));
			} else

			if(checkType(beanClazz, "org.dmg.pmml.Target") || checkType(beanClazz, "org.dmg.pmml.VerificationField") || checkType(beanClazz, "org.dmg.pmml.nearest_neighbor.InstanceField")){
				JMethod keyMethod = beanClazz.method(JMod.PUBLIC, fieldNameClass, "getKey");
				keyMethod.annotate(Override.class);
				keyMethod.body()._return(JExpr.invoke("getField"));
			} else

			if(checkType(beanClazz, "org.dmg.pmml.Value")){
				JMethod keyMethod = beanClazz.method(JMod.PUBLIC, String.class, "getKey");
				keyMethod.annotate(Override.class);
				keyMethod.body()._return(JExpr.invoke("getValue"));
			} else

			if(checkType(beanClazz, "org.dmg.pmml.association.Item") || checkType(beanClazz, "org.dmg.pmml.association.Itemset") || checkType(beanClazz, "org.dmg.pmml.sequence.Sequence") || checkType(beanClazz, "org.dmg.pmml.support_vector_machine.VectorInstance") || checkType(beanClazz, "org.dmg.pmml.text.TextDocument")){
				JMethod keyMethod = beanClazz.method(JMod.PUBLIC, String.class, "getKey");
				keyMethod.annotate(Override.class);
				keyMethod.body()._return(JExpr.invoke("getId"));
			} // End if

			if(checkType(beanClazz, "org.dmg.pmml.tree.Node")){
				JMethod hasScoreMethod = beanClazz.method(JMod.PUBLIC, boolean.class, "hasScore");
				hasScoreMethod.body()._return(JExpr.refthis("score").ne(JExpr._null()));
			}

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			FieldOutline contentField = getContentField(clazz);
			if(contentField != null){
				CPropertyInfo propertyInfo = contentField.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				JType elementType = CodeModelUtil.getElementType(fieldVar.type());

				beanClazz._implements(iterableInterface.narrow(elementType));

				JMethod iteratorMethod = beanClazz.method(JMod.PUBLIC, iteratorInterface.narrow(elementType), "iterator");
				iteratorMethod.body()._return(JExpr.invoke("get" + propertyInfo.getName(true)).invoke("iterator"));
			}

			FieldOutline extensionsField = getExtensionsField(clazz);
			if(extensionsField != null){
				beanClazz._implements(hasExtensionsInterface.narrow(beanClazz));
			}

			FieldOutline[] fields = clazz.getDeclaredFields();
			for(FieldOutline field : fields){
				CPropertyInfo propertyInfo = field.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				JMods modifiers = fieldVar.mods();
				if((modifiers.getValue() & JMod.PRIVATE) != JMod.PRIVATE){
					modifiers.setPrivate();
				}

				JType type = fieldVar.type();

				CShareableDefaultValue defaultValue = (CShareableDefaultValue)propertyInfo.defaultValue;
				if(defaultValue != null){

					if(defaultValue.isShared()){
						beanClazz.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, fieldVar.type(), defaultValue.getField(), defaultValue.computeInit(outline));
					}
				}

				JMethod getterMethod = beanClazz.getMethod("get" + propertyInfo.getName(true), new JType[0]);
				JMethod setterMethod = beanClazz.getMethod("set" + propertyInfo.getName(true), new JType[]{type});

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

					String paramName = param.name();

					param.name(fieldVar.name());

					JDocComment javadoc = setterMethod.javadoc();

					try {
						Field atParamsField = JDocComment.class.getDeclaredField("atParams");
						if(!atParamsField.isAccessible()){
							atParamsField.setAccessible(true);
						}

						Map<String, JCommentPart> atParams = (Map)atParamsField.get(javadoc);

						JCommentPart paramComment = atParams.remove(paramName);
						if(paramComment != null){
							atParams.put(fieldVar.name(), paramComment);
						}
					} catch(ReflectiveOperationException roe){
						throw new RuntimeException(roe);
					}

					setterMethod.body()._return(JExpr._this());
				} // End if

				if(propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(type);

					JFieldRef fieldRef = JExpr.refthis(fieldVar.name());

					JMethod hasElementsMethod = beanClazz.method(JMod.PUBLIC, boolean.class, "has" + propertyInfo.getName(true));
					hasElementsMethod.body()._return((fieldRef.ne(JExpr._null())).cand((fieldRef.invoke("size")).gt(JExpr.lit(0))));

					JMethod addElementsMethod = beanClazz.method(JMod.PUBLIC, beanClazz, "add" + propertyInfo.getName(true));
					JVar param = addElementsMethod.varParam(elementType, fieldVar.name());
					addElementsMethod.body().add(JExpr.invoke(getterMethod).invoke("addAll").arg(arraysClass.staticInvoke("asList").arg(param)));
					addElementsMethod.body()._return(JExpr._this());
				}

				Collection<JAnnotationUse> annotations = fieldVar.annotations();

				if(hasAnnotation(annotations, XmlValue.class)){
					fieldVar.annotate(XmlValueExtension.class);
				}
			}
		}

		return true;
	}

	static
	private FieldOutline getExtensionsField(ClassOutline clazz){
		FieldFilter filter = new FieldFilter(){

			@Override
			public boolean accept(CPropertyInfo propertyInfo, JType type){

				if(("extensions").equals(propertyInfo.getName(false)) && propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(type);

					return checkType(elementType, "org.dmg.pmml.Extension");
				}

				return false;
			}
		};

		return findField(clazz, filter);
	}

	static
	private FieldOutline getContentField(final ClassOutline clazz){
		FieldFilter filter = new FieldFilter(){

			private String name = clazz.implClass.name();


			@Override
			public boolean accept(CPropertyInfo propertyInfo, JType type){

				if(propertyInfo.isCollection()){
					JType elementType = CodeModelUtil.getElementType(type);

					String name = elementType.name();

					return ((this.name).equals(name + "s") || (this.name).equals(JJavaName.getPluralForm(name)));
				}

				return false;
			}
		};

		return findField(clazz, filter);
	}

	static
	private FieldOutline findField(ClassOutline clazz, FieldFilter filter){
		FieldOutline[] fields = clazz.getDeclaredFields();

		for(FieldOutline field : fields){
			CPropertyInfo propertyInfo = field.getPropertyInfo();
			JType type = field.getRawType();

			if(filter.accept(propertyInfo, type)){
				return field;
			}
		}

		return null;
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
	private boolean checkType(JType type, String fullName){
		return (type.fullName()).equals(fullName);
	}

	static
	private interface FieldFilter {

		boolean accept(CPropertyInfo propertyInfo, JType type);
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

	public static QName SUBPACKAGE_ELEMENT_NAME = new QName("http://jpmml.org/jpmml-model", "subpackage");
}
