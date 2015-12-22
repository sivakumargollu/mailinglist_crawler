package com.pramati.crawlers.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pramati.crawlers.ConversationsBuilder;
import com.pramati.crawlers.Mail;
import com.pramati.crawlers.beans.CrawlerProperties;
import com.pramati.crawlers.beans.Conversation;
import com.pramati.crawlers.beans.Utils;

/**
 * s "https://mail-archives.apache.org/mod_mbox/maven-users" and write
 * mail-content in ".txt" files according to attribute values of given
 * {@link CrawlerProperties} instance. Mails are grouped by their subject,All
 * mails with same subject will be saved under a directory. The name of the
 * directory is equals to the subject and name of ".txt" file is the combination
 * of mail-author followed by mail-sent time.
 * 
 * @author sivag
 * 
 */
public class MailingListDownLoader {

	private HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();
	private CountDownLatch latch = null;
	static ExecutorService executorService = Executors.newFixedThreadPool(30);
	CrawlerProperties crawlerProperties = null;
	static Logger logger = Logger.getLogger(MailingListDownLoader.class.getName());

	public static void main(String[] args) throws FileNotFoundException,IOException {
		MailingListDownLoader downloader = new MailingListDownLoader();
		CrawlerProperties crawlerProperties = CrawlerProperties.getInstance();
		int j = 1;
		while (j <= args.length) {
			if (j == 1)
				crawlerProperties.setYear(args[j-1]);
			if (j == 2)
				crawlerProperties.setYear(args[j-1]);
			j++;
		}
		String response  = downloader.downLoadMails(crawlerProperties);
		
		
	}

	/**
	 * 
	 * @param crawlerProperties
	 *            {@link CrawlerProperties} instance
	 * @return String message describes the execution status. Download mails and
	 *         store them under given path as text files. The following
	 *         operations will be taken place in the same sequence. <br>
	 *         1.Checks given path writable or not. If not writable then
	 *         execution process halts by returning error message. <br>
	 *         2. Starts the crawling process to determine the unique
	 *         subjects(conversations) and mails with that subject. <br>
	 *         3. Starts the crawling process so that individual mail objects of
	 *         each conversation get their mail-body from remote-website. <br>
	 *         4. Create a directory for each and every unique
	 *         subject(conversation) under which each mail will be saved as .txt
	 *         file.<br>
	 *         For completion of 2nd and 3rd steps program waits 30 and 120
	 *         seconds respectively. If not completed in stipulated time then
	 *         execution will proceeded with the available data at that moment.
	 * 
	 */
	public String downLoadMails(CrawlerProperties crawlerProperties) {
		//logger.addHandler(Utils.getFileHandler());
		logger.log(Level.INFO,"Year--" + crawlerProperties.getYear());
		logger.log(Level.INFO,"Month--" + crawlerProperties.getMonth());
		logger.log(Level.INFO,"Path--" + crawlerProperties.getPath());
		String response = "Path :"+ crawlerProperties.getPath();
		try {
		
			Utils.checkWritePermissions(crawlerProperties.getPath());
			logger.addHandler(Utils.getFileHandler());
			this.crawlerProperties = crawlerProperties;
			this.initiateCrawlingConversations();
			logger.log(Level.INFO,"Waiting to crawl conversations"+ new Date().toString());
			this.latch.await(30, TimeUnit.SECONDS);
			logger.log(Level.INFO,"Total conversations " + conversations.size());
			this.initiateCrawlingMails();
			logger.log(Level.INFO,"Waiting to crawl mails" + new Date().toString());
			this.latch.await(120, TimeUnit.SECONDS);
			this.logMails(crawlerProperties.getPath());
		} catch (ParseException e) {
			response = e.getMessage();
			logger.log(Level.SEVERE,e.getMessage()+"\n"+e.getStackTrace().toString());
		} catch (InterruptedException e) {
			response = e.getMessage();
			logger.log(Level.SEVERE,e.getMessage()+"\n"+e.getStackTrace().toString());
		} catch (IOException e) {
			response = e.getMessage();
			logger.log(Level.SEVERE,e.getMessage()+"\n"+e.getStackTrace().toString());

		}
		logger.log(Level.SEVERE,"Execution  status " + response);
		return response;
	}

	/**
	 * Using month and year attribute values, Urls will be framed so that each
	 * url can crawl a month. If a invalid month value occur i.e not between
	 * 00-12 then it will ignored. Each month url crawling will be handled as
	 * separate task by one of thread from {@link ExecutorService} pool
	 * 
	 */
	public void initiateCrawlingConversations() {
		CrawlerProperties crawlerProperties = this.crawlerProperties;
		ArrayList<String> urls = Utils.prepareTargetUrls(crawlerProperties);
		String[] months = crawlerProperties.getMonth().split(",");
		latch = new CountDownLatch(urls.size());
		for (int i = 0; i < months.length; i++) {
			String month = months[i];
			if (!Utils.isValidMonth(month.trim()))
				continue;
			try {

				ConversationsBuilder conversationsBuilder = new ConversationsBuilder();
				conversationsBuilder.setYear(crawlerProperties.getYear());
				conversationsBuilder.setMonth(month);
				conversationsBuilder.setUrl(Utils.prepareTargetUrls(
						crawlerProperties.getBaseUrl(),
						crawlerProperties.getYear(), month));
				conversationsBuilder.setConversations(conversations);
				conversationsBuilder.latch = latch;
				executorService.execute(conversationsBuilder);
			} catch (Exception e) {
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
	 *             separate file in that directory. Created directory name is
	 *             equal to the (Conversation subject)subject. File name is the
	 *             name of author followed by the datetime of mail.
	 */
	public void logMails(String path) throws ParseException, IOException {
		Utils.createDirectory(path);
		for (Map.Entry<String, Conversation> entry : conversations.entrySet()) {
			String subject = entry.getKey();
			Conversation conversation = entry.getValue();
			String conversationDirectory = path + File.separator+ conversation.getYear() + File.separator+ conversation.getMonth() + File.separator + Utils.replaceFileSystemChars(subject);
			Utils.createDirectory(conversationDirectory);
			Iterator<Mail> iterator = conversation.getMails().iterator();
			while (iterator.hasNext()) {
				Mail mail = iterator.next();
				String fileName = conversationDirectory+ File.separator+ (Utils.replaceFileSystemChars(mail.getAuthor())+ "__" + (mail.getTime())) + ".txt";
				File file = new File(fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(mail.getMailBody());
				bw.close();

			}

		}
	}

	/**
	 * Iterate throw the {@linkplain Conversation} objects and submit each mail
	 * task to executors service.
	 */
	public void initiateCrawlingMails() {

		int totalMails = Utils.getMailsCount(conversations);
		System.out.println("Total mails are ..." + totalMails);
		latch = new CountDownLatch(totalMails);
		// To avoid concurrent modification exception on conversations hashmap.
		HashMap<String, Conversation> dummy = new HashMap<String, Conversation>(
				conversations);
		Iterator<String> keyIterator = dummy.keySet().iterator();
		while (keyIterator.hasNext()) {
			Conversation conversation = conversations.get(keyIterator.next());
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
