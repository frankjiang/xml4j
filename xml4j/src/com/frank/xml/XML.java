/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. XML.java is built in 2013-4-17.
 */
package com.frank.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * XML type provides a interface of XML data operations.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class XML
{
	/**
	 * The XML file w3c document.
	 */
	protected Document	doc;

	/**
	 * Construct an instance of XML according to a specified w3c document.
	 * 
	 * @param doc
	 *            the specified w3c document
	 */
	public XML(Document doc)
	{
		this.doc = doc;
	}

	/**
	 * Construct an instance of XML according to a specified XML file.
	 * 
	 * @param file
	 *            the specified XML file
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder cannot be created which satisfies the
	 *             configuration requested
	 * @throws IOException
	 *             if I/O error occurs
	 * @throws SAXException
	 *             if any parse errors occur
	 */
	public XML(File file) throws SAXException, IOException,
			ParserConfigurationException
	{
		this(new FileInputStream(file));
	}

	/**
	 * Construct an instance of XML according to a specified input stream.
	 * 
	 * @param in
	 *            the specified input stream
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder cannot be created which satisfies the
	 *             configuration requested
	 * @throws IOException
	 *             if I/O error occurs
	 * @throws SAXException
	 *             if any parse errors occur
	 */
	public XML(InputStream in) throws SAXException, IOException,
			ParserConfigurationException
	{
		this.initialize(in);
	}

	/**
	 * Construct an instance of XML according to the name of specified XML file.
	 * 
	 * @param filename
	 *            the name of specified XML file
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder cannot be created which satisfies the
	 *             configuration requested
	 * @throws IOException
	 *             if I/O error occurs
	 * @throws SAXException
	 *             if any parse errors occur
	 */
	public XML(String filename) throws SAXException, IOException,
			ParserConfigurationException
	{
		this(new File(filename));
	}

	/**
	 * Construct an instance of XML according to the specified URI.
	 * 
	 * @param uri
	 *            the specified URI
	 * @param proxy
	 *            the Proxy through which this connection will be made. If
	 *            direct connection is desired, Proxy.NO_PROXY should be
	 *            specified. A null value will be regarded as using system
	 *            proxy.
	 * @param timeout
	 *            an <tt>int</tt> that specifies the timeout value to be used in
	 *            milliseconds
	 * @throws IOException
	 *             if I/O error occurs
	 * @throws SAXException
	 *             if any parse errors occur
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder cannot be created which satisfies the
	 *             configuration requested
	 */
	public XML(URI uri, Proxy proxy, int timeout) throws IOException,
			SAXException, ParserConfigurationException
	{
		this(uri.toURL(), proxy, timeout);
	}

	/**
	 * Construct an instance of XML according to the specified URL.
	 * 
	 * @param url
	 *            the specified URL
	 * @param proxy
	 *            the Proxy through which this connection will be made. If
	 *            direct connection is desired, Proxy.NO_PROXY should be
	 *            specified. A null value will be regarded as using system
	 *            proxy.
	 * @param timeout
	 *            an <tt>int</tt> that specifies the timeout value to be used in
	 *            milliseconds
	 * @throws IOException
	 *             if I/O error occurs
	 * @throws SAXException
	 *             if any parse errors occur
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder cannot be created which satisfies the
	 *             configuration requested
	 */
	public XML(URL url, Proxy proxy, int timeout) throws IOException,
			SAXException, ParserConfigurationException
	{
		URLConnection urlc = proxy == null ? url.openConnection() : url
				.openConnection(proxy);
		urlc.setReadTimeout(timeout);
		this.initialize(urlc.getInputStream());
	}

	/**
	 * Returns the w3c document for the current XML data.
	 * 
	 * @return the w3c document
	 */
	public Document getDocument()
	{
		return this.doc;
	}

	/**
	 * Returns the <code>Element</code> that has an ID attribute with the given
	 * value. If no such element exists, this returns <code>null</code> . If
	 * more than one element has an ID attribute with that value, what is
	 * returned is undefined. <br>
	 * The DOM implementation is expected to use the attribute
	 * <code>Attr.isId</code> to determine if an attribute is of type ID.
	 * <p >
	 * <b>Note:</b> Attributes with the name "ID" or "id" are not of type ID
	 * unless so defined.
	 * 
	 * @param id
	 *            The unique <code>id</code> value for an element.
	 * @return The matching element or <code>null</code> if there is none.
	 */
	public Element getElementById(String id)
	{
		return this.doc.getElementById(id);
	}

	/**
	 * Initialize the XML data according to a specified input stream.
	 * 
	 * @param in
	 *            the specified input stream
	 * @throws ParserConfigurationException
	 *             if a DocumentBuilder cannot be created which satisfies the
	 *             configuration requested
	 * @throws IOException
	 *             if I/O error occurs
	 * @throws SAXException
	 *             if any parse errors occur
	 */
	protected void initialize(InputStream in) throws SAXException, IOException,
			ParserConfigurationException
	{
		this.doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(in);
	}
}
