package com.pramati.crawlers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pramati.crawlers.beans.Utils;

/**
 * 
 * @author sivag
 * Each {@link Mail} object act as thread and retrieves its mail-body from  remote using "id" attribute. This attribute is unique to each and every mail
 * If a mail object has id "123456".
 * Then mail content will retrieved from the URL= url.123456
 * Example : https://mail-archives.apache.org/mod_mbox/maven-users/YEARMONTH.mbox/ajax/<123456> 
 */

public class Mail extends Crawlable {

	private String id = "";
	private String time = "";
	private String author = "";
	private String mailBody = "";

	public void run() {
		boolean countDown = false;
		try {
			Connection.Response response = this.getResponse(this.url+this.getId());
			this.setMailBody(this.extractMailBody(response.body()));
			if (!countDown) {
				latch.countDown();
				countDown = true;
			}
		} catch (Exception e) {
			if (!countDown) {
				countDown = true;
				latch.countDown();
			}
			e.printStackTrace();

		} finally {
			if (!countDown) {
				countDown = true;
				latch.countDown();
			}
		}
		System.out.println("Processing mail by " + Thread.currentThread().getName()
				+ " ,Latch count " + latch.getCount()+"...");
	}

	/**
	 * 
	 * @return It will parse the response and extract the body of mail from
	 *         given response.I.e <mailBody> Tag from the xml.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public String extractMailBody(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		String content = "NA";
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputStream);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("mail");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				content = Utils.getValue("contents", e);
			}
		}
		return content;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the mailBody
	 */
	public String getMailBody() {
		return mailBody;
	}

	/**
	 * @param mailBody
	 *            the mailBody to set
	 */
	public void setMailBody(String content) {
		this.mailBody = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mail other = (Mail) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}
