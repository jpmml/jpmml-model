/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model.filters;

import org.dmg.pmml.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

abstract
public class PMMLFilter extends XMLFilterImpl {

	private String sourceNamespaceURI = null;

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

		if(isFilterable(namespaceURI)){
			updateSource(namespaceURI);

			String filteredLocalName = filterLocalName(localName);
			String filteredQualifiedName = (("").equals(qualifiedName) ? "" : filteredLocalName);

			Attributes filteredAttributes = filterAttributes(localName, attributes);

			super.startElement(getNamespaceURI(), filteredLocalName, filteredQualifiedName, filteredAttributes);

			return;
		}

		super.startElement(namespaceURI, localName, qualifiedName, attributes);
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {

		if(isFilterable(namespaceURI)){
			String filteredLocalName = filterLocalName(localName);
			String filteredQualifiedName = (("").equals(qualifiedName) ? "" : filteredLocalName);

			super.endElement(getNamespaceURI(), filteredLocalName, filteredQualifiedName);

			return;
		}

		super.endElement(namespaceURI, localName, qualifiedName);
	}

	private boolean isFilterable(String namespaceURI){

		if(("").equals(namespaceURI)){
			return true;
		} // End if

		if(this.sourceNamespaceURI != null && (this.sourceNamespaceURI).equals(namespaceURI)){
			return true;
		}

		return namespaceURI.startsWith("http://www.dmg.org/PMML-");
	}

	private String getNamespaceURI(){
		Version target = getTarget();

		return target.getNamespaceURI();
	}

	private void updateSource(String namespaceURI){

		if(("").equals(namespaceURI)){
			return;
		} // End if

		if(this.sourceNamespaceURI != null && (this.sourceNamespaceURI).equals(namespaceURI)){
			return;
		}

		Version version = Version.forNamespaceURI(namespaceURI);

		Version source = getSource();
		if(source != null && !(source).equals(version)){
			throw new IllegalStateException();
		}

		// Keep the String reference of the namespaceURI argument, as opposed to getting one using Version#getNamespaceURI().
		// If the same String instance is reused, which is typical,
		// then String#equals(String) will be able to return quickly by performing an identity comparison
		this.sourceNamespaceURI = namespaceURI;

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

		if(target == null){
			throw new NullPointerException();
		}

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
	protected boolean hasAttribute(Attributes attributes, String localName){
		int index = attributes.getIndex("", localName);

		return (index > -1);
	}

	static
	protected String getAttribute(Attributes attributes, String localName){
		return attributes.getValue("", localName);
	}

	static
	protected Attributes setAttribute(Attributes attributes, String localName, String value){
		int index = attributes.getIndex("", localName);

		AttributesImpl result = new AttributesImpl(attributes);

		if(index < 0){
			result.addAttribute("", localName, "", "CDATA", value); // XXX
		} else

		{
			result.setValue(index, value);
		}

		return result;
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

	static
	protected Attributes removeAttribute(Attributes attributes, String localName){
		int index = attributes.getIndex("", localName);

		if(index < 0){
			return attributes;
		}

		AttributesImpl result = new AttributesImpl(attributes);
		result.removeAttribute(index);

		return result;
	}
}