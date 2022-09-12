/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMUtil {

	private DOMUtil(){
	}

	static
	public Node selectNode(byte[] bytes, String expression) throws Exception {
		Document document = parseDocument(bytes);

		XPathExpression xPathExpression = compile(document, expression);

		return (Node)xPathExpression.evaluate(document, XPathConstants.NODE);
	}

	static
	public NodeList selectNodeList(byte[] bytes, String expression) throws Exception {
		Document document = parseDocument(bytes);

		XPathExpression xPathExpression = compile(document, expression);

		return (NodeList)xPathExpression.evaluate(document, XPathConstants.NODESET);
	}

	static
	public Node getAttribute(Node node, String name){
		NamedNodeMap attributes = node.getAttributes();

		return attributes.getNamedItem(name);
	}

	static
	public String getAttributeValue(Node node, String name){
		Node attribute = getAttribute(node, name);

		if(attribute != null){
			return attribute.getNodeValue();
		}

		return null;
	}

	static
	public String[] getExtensionAttributeValues(Node node, String name){
		String[] result = new String[2];

		Node extensionAttribute = getAttribute(node, "x-" + name);
		if(extensionAttribute != null){
			result[0] = extensionAttribute.getNodeValue();
		}

		Node attribute = getAttribute(node, name);
		if(attribute != null){
			result[1] = attribute.getNodeValue();
		}

		return result;
	}

	static
	private Document parseDocument(byte[] bytes) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		try(InputStream is = new ByteArrayInputStream(bytes)){
			return documentBuilder.parse(is);
		}
	}

	static
	private XPathExpression compile(Document document, String expression) throws XPathExpressionException {
		XPathFactory xPathFactory = XPathFactory.newInstance();

		XPath xPath = xPathFactory.newXPath();
		xPath.setNamespaceContext(new DocumentNamespaceContext(document));

		return xPath.compile(expression);
	}

	static
	private class DocumentNamespaceContext implements NamespaceContext {

		private Document document = null;


		private DocumentNamespaceContext(Document document){
			setDocument(document);
		}

		@Override
		public String getNamespaceURI(String prefix){
			Document document = getDocument();

			if((XMLConstants.DEFAULT_NS_PREFIX).equals(prefix)){
				return document.lookupNamespaceURI(null);
			} // End if

			// XXX
			if(("test").equals(prefix)){
				return "http://localhost/test";
			}

			return document.lookupNamespaceURI(prefix);
		}

		@Override
		public String getPrefix(String namespaceURI){
			Document document = getDocument();

			return document.lookupPrefix(namespaceURI);
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI){
			throw new UnsupportedOperationException();
		}

		public Document getDocument(){
			return this.document;
		}

		private void setDocument(Document document){
			this.document = document;
		}
	}
}