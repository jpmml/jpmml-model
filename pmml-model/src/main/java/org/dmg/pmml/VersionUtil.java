/*
 * Copyright (c) 2024 Villu Ruusmann
 */
package org.dmg.pmml;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class VersionUtil {

	private VersionUtil(){
	}

	static
	public int compare(int[] left, int[] right){

		for(int i = 0, max = Math.max(left.length, right.length); i < max; i++){
			int diff = Integer.compare(left[i], right[i]);

			if(diff != 0){
				return diff;
			}
		}

		return 0;
	}

	static
	public int compare(String left, String right){
		return compare(parse(left), parse(right));
	}

	static
	public int[] parse(String string){
		StringTokenizer st = new StringTokenizer(string, ".");

		int[] result = new int[st.countTokens()];

		for(int i = 0; st.hasMoreTokens(); i++){
			result[i] = Integer.parseInt(st.nextToken());
		}

		return result;
	}

	static
	public Version getVersion(String function){
		return VersionUtil.functionVersions.get(function);
	}

	private static Map<String, Version> functionVersions = new LinkedHashMap<>();

	static {
		declareFunctions(Version.PMML_3_0,
			PMMLFunctions.ADD, PMMLFunctions.SUBTRACT, PMMLFunctions.MULTIPLY, PMMLFunctions.DIVIDE,
			PMMLFunctions.MIN, PMMLFunctions.MAX, PMMLFunctions.SUM, PMMLFunctions.AVG,
			PMMLFunctions.LOG10, PMMLFunctions.LN, PMMLFunctions.SQRT, PMMLFunctions.ABS,
			PMMLFunctions.UPPERCASE, PMMLFunctions.SUBSTRING, PMMLFunctions.TRIMBLANKS,
			PMMLFunctions.FORMATNUMBER, PMMLFunctions.FORMATDATETIME,
			PMMLFunctions.DATEDAYSSINCEYEAR, PMMLFunctions.DATESECONDSSINCEYEAR, PMMLFunctions.DATESECONDSSINCEMIDNIGHT);
		declareFunctions(Version.PMML_3_1,
			PMMLFunctions.EXP, PMMLFunctions.POW, PMMLFunctions.THRESHOLD, PMMLFunctions.FLOOR, PMMLFunctions.CEIL, PMMLFunctions.ROUND);
		declareFunctions(Version.PMML_4_0,
			PMMLFunctions.ISMISSING, PMMLFunctions.ISNOTMISSING,
			PMMLFunctions.EQUAL, PMMLFunctions.NOTEQUAL, PMMLFunctions.LESSTHAN, PMMLFunctions.LESSOREQUAL, PMMLFunctions.GREATERTHAN, PMMLFunctions.GREATEROREQUAL,
			PMMLFunctions.AND, PMMLFunctions.OR,
			PMMLFunctions.NOT,
			PMMLFunctions.ISIN, PMMLFunctions.ISNOTIN,
			PMMLFunctions.IF);
		declareFunctions(Version.PMML_4_1,
			PMMLFunctions.MEDIAN, PMMLFunctions.PRODUCT,
			PMMLFunctions.LOWERCASE);
		declareFunctions(Version.PMML_4_2,
			PMMLFunctions.CONCAT, PMMLFunctions.REPLACE, PMMLFunctions.MATCHES);
		declareFunctions(Version.PMML_4_3,
			PMMLFunctions.NORMALCDF, PMMLFunctions.NORMALPDF, PMMLFunctions.STDNORMALCDF, PMMLFunctions.STDNORMALPDF, PMMLFunctions.ERF, PMMLFunctions.NORMALIDF, PMMLFunctions.STDNORMALIDF);
		declareFunctions(Version.PMML_4_4,
			PMMLFunctions.MODULO,
			PMMLFunctions.ISVALID, PMMLFunctions.ISNOTVALID,
			PMMLFunctions.EXPM1, PMMLFunctions.HYPOT, PMMLFunctions.LN1P, PMMLFunctions.RINT,
			PMMLFunctions.STRINGLENGTH,
			PMMLFunctions.SIN, PMMLFunctions.ASIN, PMMLFunctions.SINH, PMMLFunctions.COS, PMMLFunctions.ACOS, PMMLFunctions.COSH, PMMLFunctions.TAN, PMMLFunctions.ATAN, PMMLFunctions.TANH);
		declareFunctions(Version.XPMML,
			PMMLFunctions.ATAN2);
	}

	static
	private void declareFunctions(Version version, String... functions){

		for(String function : functions){
			VersionUtil.functionVersions.put(function, version);
		}
	}
}