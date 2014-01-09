/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Test.java is built in 2013-4-17.
 */
package com.frank.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Main.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Test
{
	public static void display(int level, Node node)
	{
		if (node == null)
			return;
		if (node.getNodeValue() != null)
			System.out.printf("%02d:%s\r\n", level, node.getNodeValue());
		if (node.hasChildNodes())
		{
			NodeList list = node.getChildNodes();
			int size = list.getLength();
			level++;
			for (int index = 0; index < size; index++)
				Test.display(level, list.item(index));
		}
	}

	public static void display(NodeList list)
	{
		int size = list.getLength();
		for (int index = 0; index < size; index++)
		{
			Node node = list.item(index);
			System.out
					.printf("Node: %s\r\nAttributes:\r\n", node.getNodeName());
			NamedNodeMap map = node.getParentNode().getAttributes();
			for (int i = 0; i < map.getLength(); i++)
				System.out.printf("%s - %s\r\n", map.item(i).getNodeName(), map
						.item(i).getNodeValue());
			System.out.println("Values:");
			Test.show(node.getChildNodes());
			System.out.println("---------------------------------");
		}
	}

	/**
	 * 返回一段XML表述的错误信息。提示信息的TITLE为：系统错误。之所以使用字符串拼装，主要是这样做一般 不会有异常出现。
	 * 
	 * @param errMsg
	 *            提示错误信息
	 * @return a XML String show err msg
	 */
	public static String errXMLString(String errMsg)
	{
		StringBuffer msg = new StringBuffer(100);
		msg.append("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
		msg.append("<errNode title=\"系统错误\" errMsg=\"" + errMsg + "\"/>");
		return msg.toString();
	}

	/**
	 * 返回一段XML表述的错误信息。提示信息的TITLE为：系统错误
	 * 
	 * @param errMsg
	 *            提示错误信息
	 * @param errClass
	 *            抛出该错误的类，用于提取错误来源信息。
	 * @return a XML String show err msg
	 */
	public static String errXMLString(String errMsg, Class errClass)
	{
		StringBuffer msg = new StringBuffer(100);
		msg.append("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
		msg.append("<errNode title=\"系统错误\" errMsg=\"" + errMsg
				+ "\" errSource=\"" + errClass.getName() + "\"/>");
		return msg.toString();
	}

	/**
	 * 返回一段XML表述的错误信息。
	 * 
	 * @param title
	 *            提示的title
	 * @param errMsg
	 *            提示错误信息
	 * @param errClass
	 *            抛出该错误的类，用于提取错误来源信息。
	 * @return a XML String show err msg
	 */
	public static String errXMLString(String title, String errMsg,
			Class errClass)
	{
		StringBuffer msg = new StringBuffer(100);
		msg.append("<?xml version=\"1.0\" encoding=\"gb2312\" ?>");
		msg.append("<errNode title=\"" + title + "\" errMsg=\"" + errMsg
				+ "\" errSource=\"" + errClass.getName() + "\"/>");
		return msg.toString();
	}

	/**
	 * 给定一个文件名，获取该文件并解析为一个org.w3c.dom.Document对象返回。
	 * 
	 * @param fileName
	 *            待解析文件的文件名
	 * @return a org.w3c.dom.Document
	 */
	public static Document loadXMLDocumentFromFile(String fileName)
	{
		if (fileName == null)
			throw new IllegalArgumentException("未指定文件名及其物理路径！");
		try
		{
			return Test.newDocumentBuilder().parse(new File(fileName));
		}
		catch (SAXException e)
		{
			throw new IllegalArgumentException("目标文件（" + fileName
					+ "）不能被正确解析为XML！/n" + e.getMessage());
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException("不能获取目标文件（" + fileName + "）！/n"
					+ e.getMessage());
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Main.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		XML xml = new XML(new File("conn.xml"));
		Element e = xml.getElementById("yahoo");
		NodeList list = e.getElementsByTagName("tag");
		for (int i = 0; i < list.getLength(); i++)
		{
			NamedNodeMap map = list.item(i).getAttributes();
			String id = map.getNamedItem("id").getNodeValue();
			String value = map.getNamedItem("value").getNodeValue();
			System.out.printf("%s = %s\r\n", id, value);
		}
	}

	/**
	 * 初始化一个DocumentBuilder
	 * 
	 * @return a DocumentBuilder
	 * @throws ParserConfigurationException
	 */
	public static DocumentBuilder newDocumentBuilder()
			throws ParserConfigurationException
	{
		return Test.newDocumentBuilderFactory().newDocumentBuilder();
	}

	/**
	 * 初始化一个DocumentBuilderFactory
	 * 
	 * @return a DocumentBuilderFactory
	 */
	public static DocumentBuilderFactory newDocumentBuilderFactory()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// dbf.setNamespaceAware(true);
		return dbf;
	}

	/**
	 * 获取一个Transformer对象，由于使用时都做相同的初始化，所以提取出来作为公共方法。
	 * 
	 * @return a Transformer encoding gb2312
	 */
	public static Transformer newTransformer()
	{
		try
		{
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			Properties properties = transformer.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "gb2312");
			properties.setProperty(OutputKeys.METHOD, "xml");
			properties.setProperty(OutputKeys.VERSION, "1.0");
			properties.setProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperties(properties);
			return transformer;
		}
		catch (TransformerConfigurationException tce)
		{
			throw new RuntimeException(tce.getMessage());
		}
	}

	/**
	 * 初始化一个空Document对象返回。
	 * 
	 * @return a Document
	 */
	public static Document newXMLDocument()
	{
		try
		{
			return Test.newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 给定一个节点，将该节点加入新构造的Document中。
	 * 
	 * @param node
	 *            a Document node
	 * @return a new Document
	 */
	public static Document newXMLDocument(Node node)
	{
		Document doc = Test.newXMLDocument();
		doc.appendChild(doc.importNode(node, true));
		return doc;
	}

	/**
	 * 给定一个输入流，解析为一个org.w3c.dom.Document对象返回。
	 * 
	 * @param input
	 * @return a org.w3c.dom.Document
	 */
	public static Document parseXMLDocument(InputStream input)
	{
		if (input == null)
			throw new IllegalArgumentException("参数为null！");
		try
		{
			return Test.newDocumentBuilder().parse(input);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 将传入的一个XML String转换成一个org.w3c.dom.Document对象返回。
	 * 
	 * @param xmlString
	 *            一个符合XML规范的字符串表达。
	 * @return a Document
	 */
	public static Document parseXMLDocument(String xmlString)
	{
		if (xmlString == null)
			throw new IllegalArgumentException();
		try
		{
			return Test.newDocumentBuilder().parse(
					new InputSource(new StringReader(xmlString)));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}

	public static void show(NodeList list)
	{
		int size = list.getLength();
		for (int index = 0; index < size; index++)
		{
			Node node = list.item(index);
			System.out.printf("%s = %s\r\n", node.getNodeName(),
					node.getNodeValue());
		}
	}

	/**
	 * 将传入的一个DOM Node对象输出成字符串。如果失败则返回一个空字符串""。
	 * 
	 * @param node
	 *            DOM Node 对象。
	 * @return a XML String from node
	 */
	public static String toString(Node node)
	{
		if (node == null)
			throw new IllegalArgumentException();
		Transformer transformer = Test.newTransformer();
		if (transformer != null)
			try
			{
				StringWriter sw = new StringWriter();
				transformer
						.transform(new DOMSource(node), new StreamResult(sw));
				return sw.toString();
			}
			catch (TransformerException te)
			{
				throw new RuntimeException(te.getMessage());
			}
		return Test.errXMLString("不能生成XML信息！");
	}
}
