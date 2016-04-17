/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.lang.reflect.Field;
import java.util.List;

import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.ReflectionUtil;

/**
 * <p>
 * A Visitor that interns {@link String} attribute values.
 * </p>
 *
 * <p>
 * Strings are interned using the standard {@link String#intern()} method.
 * If working with large class model objects, then it is advisable (for maximum performance) to configure the size of the string pool beforehand using JVM options <code>-XX:MaxPermSize</code> (Java 1.6) or <code>-XX:StringTableSize</code> (Java 1.7 and newer).
 * For more information, see <a href="http://java-performance.info/string-intern-in-java-6-7-8/">String.intern in Java 6, 7 and 8 – string pooling</a>.
 * </p>
 */
public class StringInterner extends AbstractSimpleVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){
		List<Field> fields = ReflectionUtil.getInstanceFields(object.getClass());

		for(Field field : fields){
			Object value = ReflectionUtil.getFieldValue(field, object);

			if(value instanceof String){
				String string = (String)value;

				ReflectionUtil.setFieldValue(field, object, intern(string));
			}
		}

		return VisitorAction.CONTINUE;
	}

	public String intern(String string){
		return string.intern();
	}
}