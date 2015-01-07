/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package javax.xml.bind;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DatatypeConverter {

	private DatatypeConverter(){
	}

	static
	public float parseFloat(String string){
		return Float.parseFloat(string);
	}

	static
	public String printFloat(float value){
		return Float.toString(value);
	}

	static
	public BigDecimal parseDecimal(String string){
		return new BigDecimal(string);
	}

	static
	public String printDecimal(BigDecimal value){
		return value.toString();
	}

	static
	public BigInteger parseInteger(String string){
		return new BigInteger(string);
	}

	static
	public String printInteger(BigInteger value){
		return value.toString();
	}
}
