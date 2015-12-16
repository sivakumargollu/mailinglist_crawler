package com.pramati.crawlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.nio.file.InvalidPathException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.pramati.crawlers.beans.Conversation;
import com.pramati.crawlers.beans.Mail;
import com.pramati.crawlers.beans.Utils;

/**
 * @author sivag
 * It dwnload mail from "https://mail-archives.apache.org/mod_mbox/maven-users" and save them in text files year and month wise
 * Mails are grouped by theire subject. All the mails in month  are comes under same conversation and will be save in single direcotry
 * 
 */
public class MavenMailingListCrawler {

	/**
	 * A conversation is identified by a subject. Each and unique subject will treated as conversation. 
	 * conversation subject and its details.
	 */
	private HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();
	private CountDownLatch latch = null;
	static ExecutorService executorService = Executors.newFixedThreadPool(30);
	ConfigProperties configProperties = null;

	public static void main(String[] args){
			MavenMailingListCrawler crawler = new MavenMailingListCrawler();
			ConfigProperties configProperties= ConfigProperties.getInstance();
			configProperties.setYear("2012");
			configProperties.setMonth("12");
			configProperties.setPath("/home/sivag/MavenMailingList");
			System.out.println("Message "+crawler.downLoadMails(configProperties));
	}
	/**
	 * 
	 * @param configProperties
	 * Download mails and store them under given path as text files.
	 * Throws {@link IOException} if given path is not writable or not accessible.
	 */
	public String downLoadMails(ConfigProperties configProperties){
		String response = "";
		try {
			System.out.println("Starting!!");
			Utils.createDirectory(configProperties.getPath());
			MavenMailingListCrawler app = new MavenMailingListCrawler();
			app.configProperties = configProperties;
			app.initiateCrawlingConversations();
			System.out.println("Waiting to crawl conversations");
			app.latch.await();
			app.initiateCrawlingMails();
			System.out.println("Waiting to crawl mails");
			app.latch.await();
			app.logMails(configProperties.getPath());
		} 
		catch(ParseException e){
			e.printStackTrace();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		catch (IOException e) {
			response  = "Failed :: Couldnot create directory at given path";
			e.printStackTrace();
			System.out.println("Failed :: Couldnot create directory at given path");
		}
		
		return response;
	}
	/**
	 *
	 * Initiate crawling process for the given year and months.
	 *  Considers only months in the format "mm" remaining all will be omitted.
	 */
	public void initiateCrawlingConversations() {
		ConfigProperties configProperties = this.configProperties;
		ArrayList<String> urls = Utils.prepareTargetUrls(configProperties);
		String[] months = configProperties.getMonth().split(",");
		latch = new CountDownLatch(urls.size());
		for(int i=0;i<months.length;i++){
			String month = months[i];
			if(!Utils.isValidMonth(month.trim()))
				continue;
			try{
			
			ConversationCrawler crawler = new ConversationCrawler();
		    crawler.setYear(configProperties.getYear());
		    crawler.setMonth(month);
			crawler.setUrl(Utils.prepareTargetUrls(configProperties.getBaseUrl(), configProperties.getYear(), month));
			crawler.setConversations(conversations);
			crawler.latch = latch;
			executorService.execute(crawler);
			}
			catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
		

	}

	/**
	 * 
	 * @param path
	 * @throws ParseException
	 * @throws IOException
	 *             Create each conversation as a directory and each mail as
	 *             seperate file in that directory Directory name is name of the
	 *             subject of the conversations File name is the name of autor
	 *             followed by the datetime of mail.
	 */
	public void logMails(String path) throws ParseException, IOException {
		Utils.createDirectory(path);
		for (Map.Entry<String, Conversation> entry : conversations.entrySet()) {
			String key = entry.getKey();
			Conversation conversation = entry.getValue();
			String conversationDirectory = path + File.separator + conversation.getMonth()+ File.separator+key;
			Utils.createDirectory(conversationDirectory);
			System.out.println(conversationDirectory);
			HashSet<Mail> mails = conversation.getMails();
			Iterator<Mail> iterator = mails.iterator();
			while (iterator.hasNext()) {
				Mail mail = iterator.next();
				File fileName = new File(conversationDirectory
						+ File.separator
						+ (Utils.replaceFileSystemChars(mail.getAuthor()) + "--"
								+ (mail.getTime())) + ".txt");
				if (!fileName.exists()) {
					System.out.println(fileName);
					fileName.createNewFile();
				}
				FileWriter fw = new FileWriter(fileName.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(mail.getMailBody());
				bw.close();

			}

		}
	}

	/**
	 * Iterate throw the {@linkplain Conversation} objects and submit each mail task to executors service.
	 */
	public void initiateCrawlingMails() {

		latch = new CountDownLatch(Utils.getMailsCount(conversations));
		for (Map.Entry<String, Conversation> entry : conversations.entrySet()) {
			Conversation conversation = entry.getValue();
			HashSet<Mail> mails = conversation.getMails();
			Iterator<Mail> iterator = mails.iterator();
			while (iterator.hasNext()) {
				Mail mail = iterator.next();
				mail.latch = latch;
				executorService.execute(mail);
			}

		}
	}

}
