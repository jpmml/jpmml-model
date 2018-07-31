/*
 * Copyright (c) 2018 Villu Ruusmann
 */
package javax.xml.namespace;

import java.io.Serializable;

public class QName implements Serializable {

	private String namespaceURI = null;

	private String localPart = null;

	private String prefix = null;


	public QName(String localPart){
		this("", localPart, "");
	}

	public QName(String namespaceURI, String localPart){
		this(namespaceURI, localPart, "");
	}

	public QName(String namespaceURI, String localPart, String prefix){
		setNamespaceURI(namespaceURI);
		setLocalPart(localPart);
		setPrefix(prefix);
	}

	public String getNamespaceURI(){
		return this.namespaceURI;
	}

	private void setNamespaceURI(String namespaceURI){
		this.namespaceURI = namespaceURI;
	}

	public String getLocalPart(){
		return this.localPart;
	}

	private void setLocalPart(String localPart){
		this.localPart = localPart;
	}

	public String getPrefix(){
		return this.prefix;
	}

	private void setPrefix(String prefix){
		this.prefix = prefix;
	}
}
