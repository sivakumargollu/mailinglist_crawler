package com.pramati.crawlers.beans;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	 * @param str
	 * @return Replaces all (/)(\) with "_" from the given string and returns
	 *         resultant
	 */
	public static String replaceFileSystemChars(String str) {
		return str.replaceAll("(\\/)", "_");
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 * @throws ParseException
	 *             "Fri, 24 Jan, 17:33" this date-format will be converted to
	 *             "Jan-24_17:33". So that this might also be used in file name.
	 */
	public static String formatDate(String datetime) throws ParseException {

		// String time = "Fri, 24 Jan, 17:33";
		Date date = dateFormat.parse(datetime);
		System.out.println(dateFormat2.format(date).toString());
		return dateFormat2.format(date).toString();

	}

	/**
	 * 
	 * @param crawlerProperties
	 * @return ArrayList<String> Generates URLs in the given below format.
	 *         "https://mail-archives.apache.org/mod_mbox/maven-users/YEARMONTH.mbox/ajax/thread"
	 * 
	 * 
	 */
	public static ArrayList<String> prepareTargetUrls(
			CrawlerProperties crawlerProperties) {
		String baseUrl = crawlerProperties.getBaseUrl();
		String[] months = crawlerProperties.getMonth().split(",");
		ArrayList<String> urls = new ArrayList<String>(11);
		//String url = "";
		for (int i = 0; i < months.length; i++) {
			if (months[i] != "" && !months[i].equals("") && months[i] != null)
				urls.add(baseUrl + "/" + crawlerProperties.getYear() + months[i]
						+ ".mbox/ajax/");
		}
		return urls;
	}

	/**
	 * 
	 * @param baseUrl
	 * @param year
	 * @param month
	 * @return //If the months contains empty or invalid values then it should
	 *         not be processed and skipped
	 */
	public static String prepareTargetUrls(String baseUrl, String year,
			String month) {
		String url = "";
		if (month != "" && !month.equals("") && Integer.parseInt(month) > 0)
			url = baseUrl + "/" + year + month + ".mbox/ajax/";
		else
			throw new InvalidPathException(
					"Could not frame url with empty month", "Empty month");
		return url;
	}

	/**
	 * 
	 * @param tag
	 * @param element
	 * @return text-value of the given node.Empty if not found
	 */
	public static String getValue(String tag, Element element) {
		String value = "";
		try {
			NodeList nodes = element.getElementsByTagName(tag).item(0)
					.getChildNodes();
			Node node = (Node) nodes.item(0);
			return node.getNodeValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static boolean createDirectory(String path) throws IOException {
		boolean created = true;
		// System.out.println("Path " + path);
		File f = new File(path);
		if (!f.exists()) {
			if (!f.mkdirs())
				throw new IOException(
						"Could not create directory at given path");
		}
		return created;

	}

	/**
	 * 
	 * @param path
	 *            If given path not existed, will creates directory with given
	 *            path and checks its permissions.
	 * @throws IOException
	 *             if given path does not have write permission.
	 * 
	 */
	public static void checkWritePermissions(String path) throws IOException {
		File f = new File(path);
		if (!f.exists())
			Utils.createDirectory(path);
		if (!f.canWrite()) {
			throw new IOException(path + "is not existed or not writable.");
		}
	}

	/**
	 * 
	 * @param conversations
	 * @return Number of mails under all conversations.
	 */
	public static int getMailsCount(HashMap<String, Conversation> conversations) {
		int count = 0;
		Iterator<String> iterator = conversations.keySet().iterator();
		while (iterator.hasNext()) {
		//	Conversation conversation = conversations.get(iterator.next()).getMails().size();
			count += conversations.get(iterator.next()).getMails().size();
		}
		iterator = null;
		return count;
	}

	/**
	 * 
	 * @param subject
	 * @return Subject is the unique for all mails of a conversation. Except that
	 *         replies mails contains Re: in the starting. This function will
	 *         remove the "Re:" or "re:" or "RE:" patterns in the starting and
	 *         returns the remaining string after trimming white spaces
	 */
	public static String nomalizeSubject(String subject) {

		String regEx = "(\\s*re\\s*:\\s*)|(\\s*RE\\s*:\\s*)|(\\s*Re\\s*:\\s*)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(subject);
		if (matcher.lookingAt()) {
			subject = subject.replaceFirst(regEx, "");
		}
		return subject.trim();
	}

	/**
	 * 
	 * @param month
	 * @return true if and only if the given string matches 00 to 12 numbers in
	 *         string format.s
	 */
	public static boolean isValidMonth(String month) {
		return month.matches("^[0][1-9]|^[1][0-2]");
	}
	/**
	 * Convert html chars to proper text characters.
	 * @param str
	 * @return 
	 * 
	 */
	public static String handlerHtmlChars(String str){
		return Jsoup.parse(str).text();
	}

}
