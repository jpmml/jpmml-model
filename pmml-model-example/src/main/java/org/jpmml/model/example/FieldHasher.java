/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.dmg.pmml.Field;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.visitors.AbstractVisitor;

class FieldHasher extends AbstractVisitor {

	private MessageDigest messageDigest = null;

	private Map<FieldName, FieldName> mappings = new LinkedHashMap<>();


	FieldHasher(MessageDigest messageDigest){
		setMessageDigest(messageDigest);
	}

	@Override
	public VisitorAction visit(Field<?> field){
		FieldName name = field.getName();

		if(name != null){
			this.mappings.put(name, hash(name));
		}

		return super.visit(field);
	}

	public MessageDigest getMessageDigest(){
		return this.messageDigest;
	}

	private void setMessageDigest(MessageDigest messageDigest){
		this.messageDigest = messageDigest;
	}

	public Map<FieldName, FieldName> getMappings(){
		return this.mappings;
	}

	private FieldName hash(FieldName name){
		return FieldName.create(hash(name.getValue()));
	}

	private String hash(String value){
		MessageDigest messageDigest = getMessageDigest();

		try {
			messageDigest = (MessageDigest)messageDigest.clone();
		} catch(CloneNotSupportedException cnse){
			throw new IllegalStateException(cnse);
		}

		byte[] input = value.getBytes(StandardCharsets.UTF_8);

		messageDigest.update(input);

		byte[] output = messageDigest.digest();

		return DatatypeConverter.printHexBinary(output);
	}
}