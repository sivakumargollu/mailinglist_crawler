package com.pramati.crawlers.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.pramati.crawlers.MailingListDownLoader;

	
public class CrawlerProperties {

	private String year = "2014";
	private String month = "00";
	private String baseUrl = "https://mail-archives.apache.org/mod_mbox/maven-users/";
	private String path = System.getProperty("user.home")+File.separator+"MavneMailingList";
	private String mailUrl = "https://mail-archives.apache.org/mod_mbox/maven-users/";
	
	
	private CrawlerProperties(){
		
	}

	public String getMailUrl() {
		return mailUrl;
	}

	/**
	 * 
	 * @param mailUrl
	 * @deprecated mailUrl cannot be changed and it <b> It dynamically framer </b> Based on conversation year and month
	 * Fixed to "https://mail-archives.apache.org/mod_mbox/maven-users/YEARMONTH.mbox/ajax/";
	 */
	public void setMailUrl(String mailUrl) {
		// this.mailUrl = mailUrl;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *  The path to set File system path to write mails in .txt files.
	 *  Must be writable. If not given then a directory with name "MavneMailingList" will be created under users home directory. 
	 *  User of running this program should have privilege to create the directory under user.home.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		if (year.matches("[\\d]{4}"))
			this.year = year;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * 
	 * @param baseUrl
	 * @deprecated baseUrl couldnot be changed. Fixed to
	 *             https://mail-archives.apache.org/mod_mbox/maven-users/
	 * 
	 */
	public void setBaseUrl(String baseUrl) {
		// this.baseUrl = baseUrl;
	}

	/**
	 * @param month
	 * Months to set,
	 *  00 for all months.
	 *   01,02,03,...etc for specific months between (01-12). 
	 *   Months in any other format will be ignored
	 *           
	 */
	public void  setMonth(String month) {
		if(month.trim().equals("00"))
			this.month = "01,02,03,04,05,06,07,08,09,10,11,12";
		else 
			this.month = month;

	}

	/**
	 * 
	 * @return CrawlerProperties Reads cofig.properties file and populate the
	 *         values in CrawlerProperties If it fails to read the properties
	 *         file then default values will be populated.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static CrawlerProperties getInstance(){

		CrawlerProperties crawlerProperties = new CrawlerProperties();
		FileInputStream inputStream = null;
		Properties properties = new Properties();
		try {
			 File jarPath=new File(CrawlerProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		     String propertiesPath=jarPath.getParentFile().getAbsolutePath();
		     System.out.println(" propertiesPath-"+propertiesPath+"/crawler.properties");
		     properties.load(new FileInputStream(propertiesPath+"/crawler.properties"));
			
		} catch (FileNotFoundException e) {
			System.out.println("Properties file not available,Contine with default settings");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch(NullPointerException e){ 
			System.out.println(e.getMessage());
		}
		if(properties.getProperty("month") != null && properties.getProperty("month").trim() != "")
		   crawlerProperties.setMonth(properties.getProperty("month"));
		else
		   crawlerProperties.setMonth("00");
		if(properties.getProperty("year") != null && properties.getProperty("year").trim() != "")
		crawlerProperties.setYear(properties.getProperty("year"));
		if(properties.getProperty("logpath") != null && properties.getProperty("logpath").trim() != "")
		crawlerProperties.setPath(properties.getProperty("logpath"));
		return crawlerProperties;

	}

}
