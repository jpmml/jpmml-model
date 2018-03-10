/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

public class StringUtil {

	private StringUtil(){
	}

	/**
	 * <p>
	 * Trims trailing whitespace from a string value.
	 * According to the PMML specification,
	 * the leading whitespace is significant, but the trailing whitespace isn't.
	 * </p>
	 *
	 * @see Character#isWhitespace(char)
	 */
	static
	public String trim(String string){
		int length = string.length();

		int trimmedLength = length;

		while(trimmedLength > 0){
			char c = string.charAt(trimmedLength - 1);

			if(!Character.isWhitespace(c)){
				break;
			}

			trimmedLength--;
		}

		if(trimmedLength < length){
			string = string.substring(0, trimmedLength);
		}

		return string;
	}
}