/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.util.List;
import java.util.ListIterator;

import org.dmg.pmml.Annotation;
import org.dmg.pmml.Extension;
import org.dmg.pmml.HasMixedContent;
import org.dmg.pmml.Row;
import org.dmg.pmml.Timestamp;
import org.dmg.pmml.VisitorAction;

public class MixedContentCleaner extends AbstractVisitor {

	@Override
	public VisitorAction visit(Annotation annotation){
		clean(annotation);

		return super.visit(annotation);
	}

	@Override
	public VisitorAction visit(Extension extension){
		clean(extension);

		return super.visit(extension);
	}

	@Override
	public VisitorAction visit(Row row){
		clean(row);

		return super.visit(row);
	}

	@Override
	public VisitorAction visit(Timestamp timestamp){
		clean(timestamp);

		return super.visit(timestamp);
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