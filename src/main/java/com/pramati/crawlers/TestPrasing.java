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

import com.pramati.crawlers.beans.Utils;

public class TestPrasing {

	public static void main(String args[]) throws IOException {
		String str = "/home/sivag/logs/01/Maven Setup for Existing Plugin Development Project(Eclips\\e Plugin)/Singh, Harsimranjit (NSN - I/angalore)--Mon, 30 Jan, 05:16.txt";
//		File f = new File(str);
//		f.createNewFile();
		System.out.println(Utils.isValidMonth("13"));
	}

}
