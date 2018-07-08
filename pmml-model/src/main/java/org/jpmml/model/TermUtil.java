/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package org.jpmml.model;

public class TermUtil {

	private TermUtil(){
	}

	static
	public String trimPunctuation(String string){
		int begin = 0;
		int end = string.length();

		// Trim leading punctuation
		while(begin < end){
			char c = string.charAt(begin);

			if(!isPunctuation(c)){
				break;
			}

			begin++;
		}

		// Trim trailing punctuation
		while(end > begin){
			char c = string.charAt(end - 1);

			if(!isPunctuation(c)){
				break;
			}

			end--;
		}

		if(begin > 0 || end < string.length()){
			string = string.substring(begin, end);
		}

		return string;
	}

	static
	public boolean isPunctuation(char c){
		int type = Character.getType(c);

		switch(type){
			case Character.DASH_PUNCTUATION:
			case Character.END_PUNCTUATION:
			case Character.START_PUNCTUATION:
			case Character.CONNECTOR_PUNCTUATION:
			case Character.OTHER_PUNCTUATION:
			case Character.INITIAL_QUOTE_PUNCTUATION:
			case Character.FINAL_QUOTE_PUNCTUATION:
				return true;
			default:
				return false;
		}
	}
}