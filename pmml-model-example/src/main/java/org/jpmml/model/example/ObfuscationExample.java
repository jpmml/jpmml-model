/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.example;

import java.security.MessageDigest;
import java.util.Map;

import com.beust.jcommander.Parameter;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.model.visitors.FieldRenamer;

public class ObfuscationExample extends TransformationExample {

	@Parameter (
		names = "--algorithm",
		description = "Message digest algorithm"
	)
	private String algorithm = "MD5";


	static
	public void main(String... args) throws Exception {
		execute(ObfuscationExample.class, args);
	}

	@Override
	public PMML transform(PMML pmml) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(this.algorithm);

		FieldHasher fieldHasher = new FieldHasher(messageDigest);
		fieldHasher.applyTo(pmml);

		Map<FieldName, FieldName> mappings = fieldHasher.getMappings();

		FieldRenamer fieldRenamer = new FieldRenamer(mappings);
		fieldRenamer.applyTo(pmml);

		return pmml;
	}
}