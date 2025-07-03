/*
 * Copyright (c) 2025 Villu Ruusmann
 */
package org.teavm.classlib.javax.xml.namespace;

import org.teavm.classlib.java.io.TSerializable;
import org.teavm.classlib.java.lang.TString;

public class TQName implements TSerializable {

	private TString namespaceURI = null;

	private TString localPart = null;

	private TString prefix = null;


	public TQName(TString localPart){
		this(new TString(""), localPart, new TString(""));
	}

	public TQName(TString namespaceURI, TString localPart){
		this(namespaceURI, localPart, new TString(""));
	}

	public TQName(TString namespaceURI, TString localPart, TString prefix){
		setNamespaceURI(namespaceURI);
		setLocalPart(localPart);
		setPrefix(prefix);
	}

	public TString getNamespaceURI(){
		return this.namespaceURI;
	}

	private void setNamespaceURI(TString namespaceURI){
		this.namespaceURI = namespaceURI;
	}

	public TString getLocalPart(){
		return this.localPart;
	}

	private void setLocalPart(TString localPart){
		this.localPart = localPart;
	}

	public TString getPrefix(){
		return this.prefix;
	}

	private void setPrefix(TString prefix){
		this.prefix = prefix;
	}
}