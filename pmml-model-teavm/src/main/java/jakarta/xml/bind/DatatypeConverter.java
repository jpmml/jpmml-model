/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package jakarta.xml.bind;

public class DatatypeConverter {

	private DatatypeConverter(){
	}

	static
	public int parseInt(String string){
		return Integer.parseInt(string);
	}

	static
	public String printInt(int value){
		return Integer.toString(value);
	}

	static
	public double parseDouble(String string){
		return Double.parseDouble(string);
	}

	static
	public String printDouble(double value){
		return Double.toString(value);
	}

	static
	public float parseFloat(String string){
		return Float.parseFloat(string);
	}

	static
	public String printFloat(float value){
		return Float.toString(value);
	}
}