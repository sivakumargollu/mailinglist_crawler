package com.pramati.crawlers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestPrasing {

	public static void main(String args[]) throws ParserConfigurationException,
			SAXException, IOException {
		File f = new File("/home/sivag/Desktop/EclipseProjects/sampleXMl.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(f);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("index");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				System.out.println("page..." + e.getAttribute("page"));
				System.out.println("pages..." + e.getAttribute("pages"));
				NodeList messages = e.getElementsByTagName("message");
				for (int j = 0; j < messages.getLength(); j++) {
					Node messageNode = messages.item(j);
					if (messageNode.getNodeType() == messageNode.ELEMENT_NODE) {
						Element messageElement = (Element) messageNode;
						System.out.println("id..."
								+ messageElement.getAttribute("id"));
						System.out.println("from "+getValue("from", messageElement));
						System.out.println("subject.."+getValue("subject", messageElement));
						System.out.println("date..."+getValue("date", messageElement));

					}
				}

			}

		}

	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}

}
