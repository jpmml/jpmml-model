/*
 * Copyright (c) 2010 University of Tartu
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.xjc;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CAdapter;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;

public class ValueConstructorPlugin extends Plugin {

	@Override
	public String getOptionName(){
		return "XvalueConstructor";
	}

	@Override
	public String getUsage(){
		return null;
	}

	@Override
	@SuppressWarnings (
		value = {"unused"}
	)
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler){
		JCodeModel codeModel = outline.getCodeModel();

		JClass alternateValueConstructorAnnotation = codeModel.ref("org.jpmml.model.annotations.AlternateValueConstructor");
		JClass propertyAnnotation = codeModel.ref("org.jpmml.model.annotations.Property");
		JClass valueConstructorAnnotation = codeModel.ref("org.jpmml.model.annotations.ValueConstructor");

		JClass objectClass = codeModel.ref(Object.class);
		JClass fieldClass = codeModel.ref("org.dmg.pmml.Field").narrow(objectClass.wildcard());

		Collection<? extends ClassOutline> classOutlines = outline.getClasses();
		for(ClassOutline classOutline : classOutlines){
			JDefinedClass beanClazz = classOutline.implClass;

			Map<String, JFieldVar> fieldVars = beanClazz.fields();

			FieldOutline[] fieldOutlines = getRequiredFields(beanClazz, classOutline);
			if(fieldOutlines.length == 0){
				continue;
			}

			JMethod defaultConstructor = beanClazz.getConstructor(new JType[0]);
			if(defaultConstructor == null){
				defaultConstructor = beanClazz.constructor(JMod.PUBLIC);
			}

			JMethod valueConstructor = beanClazz.constructor(JMod.PUBLIC);
			valueConstructor.annotate(valueConstructorAnnotation);

			boolean hasFieldName = false;

			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				hasFieldName |= isFieldName(outline, propertyInfo);

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				JVar param = valueConstructor.param(fieldVar.type(), fieldVar.name());

				param.annotate(propertyAnnotation).param("value", fieldVar.name());

				valueConstructor.body().assign(JExpr.refthis(fieldVar.name()), param);
			}

			if(!hasFieldName){
				continue;
			}

			JClass beanSuperClazz = beanClazz._extends();
			if(checkType(beanSuperClazz.erasure(), "org.dmg.pmml.Field")){
				continue;
			}

			JMethod alternateValueConstructor = beanClazz.constructor(JMod.PUBLIC);
			alternateValueConstructor.annotate(alternateValueConstructorAnnotation);

			JInvocation invocation = (alternateValueConstructor.body()).invoke("this");

			for(FieldOutline fieldOutline : fieldOutlines){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				JFieldVar fieldVar = fieldVars.get(propertyInfo.getName(false));

				if(isFieldName(outline, propertyInfo)){
					String name = fieldVar.name();

					if(!(name).equals("field") && !name.endsWith("Field")){
						name += "Field";
					}

					JVar param = alternateValueConstructor.param(fieldClass, name);

					invocation.arg(JOp.cond(param.ne(JExpr._null()), param.invoke("getName"), JExpr._null()));
				} else

				{
					JVar param = alternateValueConstructor.param(fieldVar.type(), fieldVar.name());

					invocation.arg(param);
				}
			}
		}

		return true;
	}

	static
	private FieldOutline[] getRequiredFields(JDefinedClass beanClazz, ClassOutline classOutline){
		Predicate<FieldOutline> predicate = new Predicate<FieldOutline>(){

			@Override
			public boolean test(FieldOutline fieldOutline){
				CPropertyInfo propertyInfo = fieldOutline.getPropertyInfo();

				boolean required = OutlineUtil.isRequired(beanClazz, propertyInfo);

				List<CPluginCustomization> propertyCustomizations = CustomizationUtil.findPropertyCustomizationsInProperty(propertyInfo, AnnotatePlugin.ANNOTATE_PROPERTY_QNAME);
				for(CPluginCustomization propertyCustomization : propertyCustomizations){
					String[] classAndValue = AnnotatePlugin.parseCustomization(propertyCustomization);

					switch(classAndValue[0]){
						case "org.jpmml.model.annotations.ValueConstructorParameter":

							if(classAndValue.length > 1){

								switch(classAndValue[1]){
									case "false":
										return false;
									case "true":
										break;
									default:
										throw new IllegalArgumentException();
								}
							}

							required |= true;
							break;
						default:
							break;
					}
				}

				return required;
			}
		};

		return Arrays.stream(classOutline.getDeclaredFields())
			.filter(predicate)
			.toArray(FieldOutline[]::new);
	}

	static
	private boolean isFieldName(Outline outline, CPropertyInfo propertyInfo){
		CAdapter adapter = propertyInfo.getAdapter();

		if(adapter != null){
			JClass adapterClass = adapter.getAdapterClass(outline);

			return checkType(adapterClass, "org.dmg.pmml.adapters.FieldNameAdapter");
		}

		return false;
	}

	static
	private boolean checkType(JType type, String fullName){
		return (type.fullName()).equals(fullName);
	}
}