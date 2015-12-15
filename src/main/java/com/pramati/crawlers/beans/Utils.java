package com.pramati.crawlers.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.pramati.crawlers.ConfigProperties;

public class Utils {

	static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE, d MMM, h:mm");
	static SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM-dd'_'HH:mm");

	/**
	 * 
	 * @param str
	 * @return Replaces all white characters from the given string and returns
	 *         resultant
	 */
	public static String replaceWhiteChars(String str) {
		return str.replaceAll("[\\s]", "_");
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 * @throws ParseException
	 *             "Fri, 24 Jan, 17:33" this date-format will be converted to
	 *             "Jan-24_17:33". So that this  might also be used in file
	 *             name.
	 */
	public static String formatDate(String datetime) throws ParseException {

		// String time = "Fri, 24 Jan, 17:33";
		Date date = dateFormat.parse(datetime);
		System.out.println(dateFormat2.format(date).toString());
		return dateFormat2.format(date).toString();

	}
	/**
	 * 
	 * @param configProperties
	 * @return ArrayList<String>
	 *  Generates URLs in the given below format.
	 * "https://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/ajax/thread?0"
	 * 
	 */
	public static ArrayList<String> prepareTargetUrls(ConfigProperties configProperties){
		String baseUrl = configProperties.getBaseUrl();
		String[] months = configProperties.getMonth().split(",");
		ArrayList<String> urls = new ArrayList<String>(11);
		String url = "";
		for (int i = 0; i < months.length; i++) {
			urls.add(baseUrl+"/"+configProperties.getYear()+months[i]+".mbox/ajax/thread");
		}
		return urls;
	}
	/**
	 * 
	 * @param tag
	 * @param element
	 * @return text-value of the given node.Empty if not found
	 */
	public static String getValue(String tag, Element element) {
		String value = "";
		try{
		NodeList nodes = element.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
}
