/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

abstract
public class PMMLFilter extends XMLFilterImpl {

	private Version source = null;

	private Version target = null;


	public PMMLFilter(Version target){
		setTarget(target);
	}

	public PMMLFilter(XMLReader reader, Version target){
		super(reader);

		setTarget(target);
	}

	abstract
	public String filterLocalName(String localName);

	abstract
	public Attributes filterAttributes(String localName, Attributes attributes);

	@Override
	public void startPrefixMapping(String prefix, String namespaceURI) throws SAXException {

		if(("").equals(prefix)){
			updateSource(namespaceURI);

			super.startPrefixMapping("", getNamespaceURI());

			return;
		}

		super.startPrefixMapping(prefix, namespaceURI);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		super.endPrefixMapping(prefix);
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
		updateSource(namespaceURI);

		String filteredNamespaceURI = getNamespaceURI();
		String filteredLocalName = filterLocalName(localName);

		String filteredQualifiedName = formatQualifiedName(qualifiedName, filteredNamespaceURI, filteredLocalName);

		Attributes filteredAttributes = filterAttributes(localName, attributes);

		super.startElement(filteredNamespaceURI, filteredLocalName, filteredQualifiedName, filteredAttributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
		String filteredNamespaceURI = getNamespaceURI();
		String filteredLocalName = filterLocalName(localName);

		String filteredQualifiedName = formatQualifiedName(qualifiedName, filteredNamespaceURI, filteredLocalName);

		super.endElement(filteredNamespaceURI, filteredLocalName, filteredQualifiedName);
	}

	private String getNamespaceURI(){
		Version target = getTarget();

		return target.getNamespaceURI();
	}

	private void updateSource(String namespaceURI){

		if(("").equals(namespaceURI)){
			return;
		}

		Version version = Version.forNamespaceURI(namespaceURI);

		Version source = getSource();
		if(source != null && !(source).equals(version)){
			throw new IllegalStateException();
		}

		setSource(version);
	}

	public Version getSource(){
		return this.source;
	}

	private void setSource(Version source){
		this.source = source;
	}

	public Version getTarget(){
		return this.target;
	}

	private void setTarget(Version target){
		this.target = target;
	}

	static
	protected int compare(Version left, Version right){

		if(left == null || right == null){
			throw new IllegalStateException();
		}

		return (left).compareTo(right);
	}

	static
	private String formatQualifiedName(String template, String namespaceURI, String localName){

		if(("").equals(template)){
			return "";
		} // End if

		if(template.indexOf(':') > -1){
			return (namespaceURI + ":" + localName);
		}

		return localName;
	}

	static
	protected boolean hasAttribute(Attributes attributes, String localName){
		int index = attributes.getIndex("", localName);

		return (index > -1);
	}

	static
	protected Attributes renameAttribute(Attributes attributes, String oldLocalName, String localName){
		int index = attributes.getIndex("", oldLocalName);
		if(index < 0){
			return attributes;
		}

		AttributesImpl result = new AttributesImpl(attributes);
		result.setLocalName(index, localName);
		result.setQName(index, localName); // XXX

		return result;
	}
}