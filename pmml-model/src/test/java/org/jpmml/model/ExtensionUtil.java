/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model;

import java.util.List;

import org.dmg.pmml.Extension;
import org.dmg.pmml.HasExtensions;

import static org.junit.Assert.assertEquals;

public class ExtensionUtil {

	private ExtensionUtil(){
	}

	static
	public List<?> getContent(HasExtensions<?> hasExtensions){
		List<Extension> extensions = hasExtensions.getExtensions();

		assertEquals(1, extensions.size());

		Extension extension = extensions.get(0);

		return extension.getContent();
	}
}