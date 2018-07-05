/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * <p>
 * A SAX filter that removes whitespace strings.
 * </p>
 */
public class WhitespaceFilter extends XMLFilterImpl {

	public WhitespaceFilter(){
	}

	public WhitespaceFilter(XMLReader reader){
		super(reader);
	}

	@Override
	public void characters(char[] buffer, int offset, int length) throws SAXException {

		for(int i = offset, max = offset + length; i < max; i++){
			char c = buffer[i];

			if(c > ' '){
				super.characters(buffer, offset, length);

				return;
			}
		}
	}

	@Override
	public void ignorableWhitespace(char[] buffer, int offset, int length) throws SAXException {
	}
}