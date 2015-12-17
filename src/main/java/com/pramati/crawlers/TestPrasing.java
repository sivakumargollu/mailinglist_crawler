package com.pramati.crawlers;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
		if(args.length > 0){
			System.out.println("Program have arguments");
			
		}
		else{
			System.out.println("Program doesnt have arguments");
		}
		for (String s: args) {
            System.out.println(s);
        }
	}
}
