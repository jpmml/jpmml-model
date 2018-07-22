/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;
import java.util.ListIterator;

import org.dmg.pmml.HasMixedContent;
import org.dmg.pmml.PMMLObject;
import org.dmg.pmml.VisitorAction;

public class MixedContentCleaner extends AbstractVisitor {

	@Override
	public VisitorAction visit(PMMLObject object){

		if(object instanceof HasMixedContent){
			HasMixedContent<?> hasMixedContent = (HasMixedContent<?>)object;

			clean(hasMixedContent);
		}

		return super.visit(object);
	}

	private void clean(HasMixedContent<?> hasMixedContent){

		if(hasMixedContent.hasContent()){
			List<Object> content = hasMixedContent.getContent();

			for(ListIterator<Object> it = content.listIterator(); it.hasNext(); ){
				Object object = it.next();

				if(object instanceof String){
					String string = (String)object;

					string = string.trim();

					if(("").equals(string)){
						it.remove();
					}
				}
			}
		}
	}
}