/*
 * Copyright (c) 2012 University of Tartu
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dmg.pmml.Array;
import org.dmg.pmml.ComplexValue;

public class ArrayUtil {

	private ArrayUtil(){
	}

	static
	public List<String> parse(Array.Type type, String string){
		List<String> result = new ArrayList<>(Math.max(16, string.length() / 8));

		StringBuilder sb = new StringBuilder();

		boolean enableQuotes;

		switch(type){
			case INT:
			case REAL:
				enableQuotes = false;
				break;
			case STRING:
				enableQuotes = true;
				break;
			default:
				throw new IllegalArgumentException();
		}

		boolean quoted = false;

		tokens:
		for(int i = 0; i < string.length(); i++){
			char c = string.charAt(i);

			if(quoted){

				if(c == '\\' && i < (string.length() - 1)){
					c = string.charAt(i + 1);

					if(c == '\"'){
						sb.append('\"');

						i++;
					} else

					{
						sb.append('\\');
					}

					continue tokens;
				}

				sb.append(c);

				if(c == '\"'){
					result.add(createToken(sb, enableQuotes));

					quoted = false;
				}
			} else

			{
				if(c == '\"' && enableQuotes){

					if(sb.length() > 0){
						result.add(createToken(sb, enableQuotes));
					}

					sb.append('\"');

					quoted = true;
				} else

				if(Character.isWhitespace(c)){

					if(sb.length() > 0){
						result.add(createToken(sb, enableQuotes));
					}
				} else

				{
					sb.append(c);
				}
			}
		}

		if(sb.length() > 0){
			result.add(createToken(sb, enableQuotes));
		}

		return result;
	}

	static
	public String format(Array.Type type, Collection<?> values){
		StringBuilder sb = new StringBuilder(values.size() * 16);

		boolean enableQuotes;

		switch(type){
			case INT:
			case REAL:
				enableQuotes = false;
				break;
			case STRING:
				enableQuotes = true;
				break;
			default:
				throw new IllegalArgumentException();
		}

		for(Object value : values){

			if(value instanceof ComplexValue){
				ComplexValue complexValue = (ComplexValue)value;

				value = complexValue.toSimpleValue();
			}

			String string = value.toString();

			if(sb.length() > 0){
				sb.append(' ');
			} // End if

			if(enableQuotes){
				boolean quoted = ("").equals(string) || (string.indexOf(' ') > -1);

				if(quoted){
					sb.append('\"');
				} // End if

				if(string.indexOf('\"') > -1){

					for(int i = 0; i < string.length(); i++){
						char c = string.charAt(i);

						if(c == '\"'){
							sb.append('\\');
						}

						sb.append(c);
					}
				} else

				{
					sb.append(string);
				} // End if

				if(quoted){
					sb.append('\"');
				}
			} else

			{
				sb.append(string);
			}
		}

		return sb.toString();
	}

	static
	private String createToken(StringBuilder sb, boolean enableQuotes){
		String result;

		if(sb.length() > 1 && (sb.charAt(0) == '\"' && sb.charAt(sb.length() - 1) == '\"') && enableQuotes){
			result = sb.substring(1, sb.length() - 1);
		} else

		{
			result = sb.substring(0, sb.length());
		}

		sb.setLength(0);

		return result;
	}
}