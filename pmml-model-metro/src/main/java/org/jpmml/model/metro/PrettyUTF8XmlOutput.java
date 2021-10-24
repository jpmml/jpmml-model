/*
 * Copyright (c) 2016 Villu Ruusmann
 */
package org.jpmml.model.metro;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.stream.XMLStreamException;

import org.glassfish.jaxb.core.marshaller.MinimumEscapeHandler;
import org.glassfish.jaxb.runtime.v2.runtime.Name;
import org.glassfish.jaxb.runtime.v2.runtime.output.Encoded;
import org.glassfish.jaxb.runtime.v2.runtime.output.IndentingUTF8XmlOutput;
import org.glassfish.jaxb.runtime.v2.runtime.output.Pcdata;
import org.glassfish.jaxb.runtime.v2.runtime.output.UTF8XmlOutput;
import org.xml.sax.SAXException;

/**
 * @see IndentingUTF8XmlOutput
 */
public class PrettyUTF8XmlOutput extends UTF8XmlOutput {

	private int depth = 0;

	private boolean textWritten = false;


	public PrettyUTF8XmlOutput(OutputStream os, Encoded[] localNames){
		super(os, localNames, MinimumEscapeHandler.theInstance);
	}

	@Override
	public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
		write('\n');

		super.endDocument(fragment);
	}

	@Override
	public void beginStartTag(int prefix, String localName) throws IOException {
		indentStartTag();

		super.beginStartTag(prefix, localName);
	}

	@Override
	public void beginStartTag(Name name) throws IOException {
		indentStartTag();

		super.beginStartTag(name);
	}

	@Override
	public void endTag(int prefix, String localName) throws IOException {
		indentEndTag();

		super.endTag(prefix, localName);
	}

	@Override
	public void endTag(Name name) throws IOException {
		indentEndTag();

		super.endTag(name);
	}

	@Override
	public void text(String value, boolean needsSeparatingWhitespace) throws IOException {
		super.text(value, needsSeparatingWhitespace);

		this.textWritten = true;
	}

	@Override
	public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException {
		super.text(value, needsSeparatingWhitespace);

		this.textWritten = true;
	}

	private void indentStartTag() throws IOException {
		closeStartTag();

		if(!this.textWritten){
			printIndent();
		}

		this.depth++;

		this.textWritten = false;
	}

	private void indentEndTag() throws IOException {
		this.depth--;

		if(!super.closeStartTagPending && !this.textWritten){
			printIndent();
		}

		this.textWritten = false;
	}

	private void printIndent() throws IOException {
		write('\n');

		int length = this.depth;

		while(length > PrettyUTF8XmlOutput.indent.length){
			write(PrettyUTF8XmlOutput.indent);

			length -= PrettyUTF8XmlOutput.indent.length;
		}

		write(PrettyUTF8XmlOutput.indent, 0, length);
	}

	private static final byte[] indent = new byte[1024];

	static {
		Arrays.fill(PrettyUTF8XmlOutput.indent, (byte)'\t');
	}
}