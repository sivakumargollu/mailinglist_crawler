package com.pramati.crawlers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

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
import com.pramati.crawlers.beans.Utils;

/**
 * @author sivag
 * Base class to connect to remote.
 * {@link ConversationsBuilder} and {@link Mail} are the subclasses of the crawler.
 * 
 * 
 */
public abstract class Crawlable implements Runnable {

	
	protected String month = "";
	protected String year = "";
	

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * At which crawling must be done.
	 */
	protected String url = "";
	/**
	 * Holds the key-value pairs of conversation-subject and conversation details.
	 */
	protected HashMap<String, Conversation> conversations = null;
	/**
	 * It is  mirror the value of the pagination displaying in the web-site.
	 * Suppose there  are 500 mails available in month december and these 500 mails are arranged in 4 pages with pagination. Then this pageCount value holds that value 4
	 */
	protected int pagesCount = 3;
	protected CountDownLatch latch = null;

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
	public HashMap<String, Conversation> getConversations() {
		return conversations;
	}

	/**
	 * @param conversations
	 *            the conversations to set
	 */
	public void setConversations(HashMap<String, Conversation> conversations) {
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

}
