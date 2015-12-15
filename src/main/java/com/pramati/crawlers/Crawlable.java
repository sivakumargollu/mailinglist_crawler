package com.pramati.crawlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.rmi.CORBA.Util;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.pramati.crawlers.beans.Conversation;
import com.pramati.crawlers.beans.Mail;
import com.pramati.crawlers.beans.Utils;

/**
 * @author sivag
 * 
 */
public abstract class Crawlable implements Runnable {

	protected String url = "";
	private HashMap<String,Conversation> conversations = null;
	protected int pagesCount = 3;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the conversations
	 */
	public HashMap<String,Conversation>  getConversations() {
		return conversations;
	}

	/**
	 * @param conversations
	 *            the conversations to set
	 */
	public void setConversations(HashMap<String,Conversation>  conversations) {
		this.conversations = conversations;
	}

	/**
	 * 
	 * @param url
	 * @return {@link Response} connects to the given url and returns the
	 *         resultant text after execution.
	 */

	public Connection.Response getResponse(String url) {
		Connection.Response response = null;
		try {

			Connection connection = Jsoup.connect(url);
			connection
					.header("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:39.0) Gecko/20100101 Firefox/39.0");
			connection.maxBodySize(0);
			connection.timeout(100000);
			response = connection.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;

	}

	/***
	 * 
	 * @param xml
	 *            1. It updates the default page count new value from response
	 *            xml 2. Also it instantiate and initialize conversations
	 *            objects
	 */
	public void setConversations(String xml) {
		try {
			// File f = new
			// File("/home/sivag/Desktop/EclipseProjects/sampleXMl.xml");
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputStream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("index");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) node;
					System.out.println("page..." + e.getAttribute("page"));
					System.out.println("pages..." + e.getAttribute("pages"));
					pagesCount = Integer.parseInt(e.getAttribute("pages"));
					NodeList messages = e.getElementsByTagName("message");
					for (int j = 0; j < messages.getLength(); j++) {
						Node messageNode = messages.item(j);
						if (messageNode.getNodeType() == messageNode.ELEMENT_NODE) {
							
							Element messageElement = (Element) messageNode;
							String subject = Utils.getValue("subject",messageElement);
							Conversation conversation = this.getConversion(subject);
							Mail mail = new Mail();
							String id = messageElement.getAttribute("id");
							String from = Utils.getValue("from", messageElement);
							String date = Utils.getValue("date", messageElement);
							mail.setAuthor(from);
							mail.setId(id);
							mail.setTime(date);
							conversation.getMails().add(mail);

						}
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @param subject
	 * @return Conversation object
	 * If given subject as key not existed in the conversations then new mapping will be created and return the new conversation object
	 * if already existed then old one will be returned
	 * 
	 */
	public Conversation getConversion(String subject){
		Conversation conversation = null;
	    if(conversations.containsKey(subject)){
	    	conversation = conversations.get(subject);
	    }
	    else{
	    	conversation = new Conversation();
	    	conversation.setSubject(subject);
	    	conversations.put(subject, conversation);
	    }
	    return conversation;
		
	}

}
