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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.pramati.crawlers.beans.Conversation;
import com.pramati.crawlers.beans.Mail;
import com.pramati.crawlers.beans.Utils;

/**
 * Hello world!
 * 
 */
public class App {

	private HashMap<String,Conversation>  conversations = new HashMap<String,Conversation>();
	private CountDownLatch latch = null;

	static ExecutorService executorService = Executors.newFixedThreadPool(20);

	public static void main(String[] args) throws ParseException, IOException {
		System.out.println("Hello World!");
		ConfigProperties configProperties = ConfigProperties.getInstance();
		ArrayList<String> urls = Utils.prepareTargetUrls(configProperties);
		App app = new App();
		System.out.println(urls.toString());
		app.initiateCrawling(urls);
		app.logMails(configProperties.getPath());
		
		//System.out.println("Total conversations "+app.conversations.size());
		//System.out.println("Totail emails ..."+app.conversations)
	}

	/**
	 * 
	 * @param urls
	 *  Iterate through each url and assign the url for execution.
	 */
	public  void initiateCrawling(ArrayList urls) {
		try{
		Iterator<String> iterator = urls.iterator();
		latch = new CountDownLatch(urls.size());
		while (iterator.hasNext()) {
			String url = iterator.next();
			Crawler crawler = new Crawler();
			crawler.setUrl(url);
			crawler.setConversations(conversations);
			crawler.latch = latch;
			executorService.execute(crawler);
		}
		latch.await();
		}catch(InterruptedException e){
			e.printStackTrace();
		}	

	}
	public boolean createDirectory(String path){
		File f = new File(path);
		return f.mkdir();
	}
	public void logMails(String path) throws FileNotFoundException, UnsupportedEncodingException{
		this.createDirectory(path);
		for(Map.Entry<String, Conversation> entry : conversations.entrySet()) {
		    String key = entry.getKey();
		    Conversation conversation = entry.getValue();
		    this.createDirectory(path+"/"+key);
		    HashSet<Mail> mails = conversation.getMails();
		    Iterator<Mail> iterator = mails.iterator();
		    while(iterator.hasNext()){
		    	Mail mail = iterator.next();
		    	mail.setContent("textext");
		    	File fileName = new File(path+"/"+Utils.replaceWhiteChars(mail.getAuthor()+"--"+mail.getTime()));
		    	PrintWriter writer = new PrintWriter(Utils.replaceWhiteChars(mail.getAuthor()+mail.getTime()), "UTF-8");
		    	writer.println("textext");
		    	writer.close();
		    	
		    }
		    
		   
		}
	}
	
}
