package com.pramati.crawlers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pramati.crawlers.beans.Conversation;
import com.pramati.crawlers.beans.Mail;
import com.pramati.crawlers.beans.Utils;

public class ConversationCrawler extends Crawlable {

	/**
	 * 
	 *  String Url like
	 *         "http://mail-archives.apache.org/mod_mbox/maven-users//201101.mbox/ajax/thread"
	 *         will converted to
	 *         "http://mail-archives.apache.org/mod_mbox/maven-users//201101.mbox/ajax/thread?1"
	 *         after this appending done to the url. It will get the response from the remote web-site.
	 *        pageCount value being updated with new value taken from remote-website.
	 *         
	 */
	public void run() {
		boolean countDown = false;
		try {
			for (int pageno = 0; pageno < pagesCount; pageno++) {
				Connection.Response response = this.getResponse(url.concat("/thread?"
						+ pageno));
				this.setConversations(response.body());
			}
			latch.countDown();
			System.out.println("Latch count is" + latch.getCount());
		} catch (Exception e) {
			e.printStackTrace();
			if (!countDown)
				latch.countDown();
		} finally {
			if (!countDown)
				latch.countDown();
		}

	}
	
	
	
	
	/***
	 * It groups the all mails by subject wise.
	 * Each distinct subject will considered as a different conversation.
	 * 
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
							String subject = Utils.getValue("subject",
									messageElement);
							Conversation conversation = this
									.getConversion(subject);
							Mail mail = new Mail();
							String id = messageElement.getAttribute("id");
							String from = Utils
									.getValue("from", messageElement);
							String date = Utils
									.getValue("date", messageElement);
							mail.setAuthor(from);
							mail.setId(id);
							mail.setTime(date);
							mail.setUrl(this.url);
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
	 * @return Conversation object If given subject as key not existed in the
	 *         conversations then new mapping will be created and return the new
	 *         conversation object if already existed then old one will be
	 *         returned
	 * 
	 */
	public Conversation getConversion(String subject) {
		Conversation conversation = null;
		subject = Utils.nomalizeSubject(subject);
		if (conversations.containsKey(subject)) {
			conversation = conversations.get(subject);
		} else {
			conversation = new Conversation();
			conversation.setMonth(this.month);
			conversation.setSubject(subject);
			conversations.put(subject, conversation);
		}
		return conversation;

	}

}
