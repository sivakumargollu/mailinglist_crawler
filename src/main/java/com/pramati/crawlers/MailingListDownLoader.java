
package com.pramati.crawlers;
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

import com.pramati.crawlers.beans.CrawlerProperties;
import com.pramati.crawlers.beans.Conversation;
import com.pramati.crawlers.beans.Utils;

/**s
 *              "https://mail-archives.apache.org/mod_mbox/maven-users" and
 *              write mail-content in ".txt" files according to attribute values
 *              of given {@link CrawlerProperties} instance. Mails are grouped by
 *              their subject,All mails with same subject will be saved under a
 *              directory. The name of the directory is equals to the subject
 *              and name of ".txt" file is the combination of mail-author
 *              followed by mail-sent time.
 * @author sivag
 * 
 */
public class MailingListDownLoader {

	private HashMap<String, Conversation> conversations = new HashMap<String, Conversation>();
	private CountDownLatch latch = null;
	static ExecutorService executorService = Executors.newFixedThreadPool(30);
	CrawlerProperties crawlerProperties = null;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		MailingListDownLoader crawler = new MailingListDownLoader();
		CrawlerProperties crawlerProperties = CrawlerProperties.getInstance();
		if(args.length > 0){
			if(args[0] != "")
				crawlerProperties.setYear(args[0]);
			if(args[1] != "")
				crawlerProperties.setMonth(args[1]);
			
		}
		System.out
				.println("Message " + crawler.downLoadMails(crawlerProperties));
	}

	/**
	 * 
	 * @param crawlerProperties
	 *            {@link CrawlerProperties} instance
	 * @return String message describes the execution status.
	 * Download mails and store them under given path as text
	 *              files. The following operations will be taken place in the same
	 *              sequence. <br> 1.Checks given path writable or not. If not
	 *              writable then execution process halts by returning error
	 *              message. <br> 2. Starts the crawling process to determine the
	 *              unique subjects(conversations) and mails with that subject.
	 *              <br> 3. Starts the crawling process so that individual mail
	 *              objects of each conversation get their mail-body from
	 *              remote-website. <br> 4. Create a directory for each and every
	 *              unique subject(conversation) under which each mail will be
	 *              saved as .txt file.<br> For completion of 2nd and 3rd steps
	 *              program waits 30 and 120 seconds respectively. If not
	 *              completed in stipulated time then execution will proceeded
	 *              with the available data at that moment.
	 * 
	 */
	public String downLoadMails(CrawlerProperties crawlerProperties) {
		String response = "Downloading and files creation completed!!!";
		try {
			System.out.println("Year--"+crawlerProperties.getYear());
			System.out.println("Month--"+crawlerProperties.getMonth());
			System.out.println("Path--"+crawlerProperties.getPath());
			Utils.checkWritePermissions(crawlerProperties.getPath());
			//MailingListDownLoader app = new MailingListDownLoader();
			this.crawlerProperties = crawlerProperties;
			this.initiateCrawlingConversations();
			System.out.println("Waiting to crawl conversations"+new Date().toString());
			this.latch.await(30, TimeUnit.SECONDS);
			System.out.println("Total conversations "+conversations.size());
			this.initiateCrawlingMails();
			System.out.println("Waiting to crawl mails"+new Date().toString());
			this.latch.await(120, TimeUnit.SECONDS);
			this.logMails(crawlerProperties.getPath());
		} catch (ParseException e) {
			response = e.getMessage();
			e.printStackTrace();
		} catch (InterruptedException e) {
			response = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			response = e.getMessage();
			e.printStackTrace();
			
		}
		System.out.println("Execution  status "+response);
		return response;
	}

	/**
	 * Using month and year attribute values, Urls will be framed so that each
	 * url can crawl a month. If a invalid month value occur i.e not between
	 * 00-12 then it will ignored. Each month url crawling will be handled as
	 * separate task by one of thread  from {@link ExecutorService} pool
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
			String conversationDirectory = path + File.separator+conversation.getYear()+File.separator+conversation.getMonth() + File.separator + Utils.replaceFileSystemChars(subject);
			Utils.createDirectory(conversationDirectory);
			//System.out.println(conversationDirectory);
			//HashSet<Mail> mails = conversation.getMails();
			Iterator<Mail> iterator = conversation.getMails().iterator();
			while (iterator.hasNext()) {
				Mail mail = iterator.next();
				String fileName = conversationDirectory+ File.separator+ (Utils.replaceFileSystemChars(mail.getAuthor())+ "__" + (mail.getTime())) + ".txt";
				File file = new File(fileName);
				if (!file.exists()) {
					//System.out.println(file);
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(Utils.handlerHtmlChars(mail.getMailBody()));
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
		System.out.println("Total mails are ..."+totalMails);
		latch = new CountDownLatch(totalMails);
		//To avoid concurrent modification exception on conversations hashmap.
		HashMap<String,Conversation> dummy = new HashMap<String,Conversation>(conversations);
		Iterator<String> keyIterator = dummy.keySet().iterator();
		while(keyIterator.hasNext()) {
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
