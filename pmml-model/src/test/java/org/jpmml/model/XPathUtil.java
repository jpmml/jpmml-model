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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathUtil {

	private XPathUtil(){
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
	private Document parseDocument(byte[] bytes) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		InputStream is = new ByteArrayInputStream(bytes);

		try {
			return documentBuilder.parse(is);
		} finally {
			is.close();
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
			}

			return document.lookupNamespaceURI(prefix);
		}

		@Override
		public String getPrefix(String namespaceUri){
			Document document = getDocument();

			return document.lookupPrefix(namespaceUri);
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceUri){
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