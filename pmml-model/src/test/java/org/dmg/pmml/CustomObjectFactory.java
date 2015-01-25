/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.dmg.pmml;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class CustomObjectFactory extends ObjectFactory {

	@Override
	public CustomPMML createPMML(){
		return new CustomPMML();
	}

	@Override
	public CustomSimplePredicate createSimplePredicate(){
		return new CustomSimplePredicate();
	}
}