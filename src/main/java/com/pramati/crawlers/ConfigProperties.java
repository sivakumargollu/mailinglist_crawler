package com.pramati.crawlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

	private String year = "2014";
	private String month = "00";
	private  String baseUrl = "https://mail-archives.apache.org/mod_mbox/maven-users/";
	private String path = "/home/sivag/logs/";
	private  String mailUrl = "https://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/ajax/";
	


	public String getMailUrl() {
		return mailUrl;
	}
	/**
	 * 
	 * @param mailUrl
	 * @deprecated
	 * mailUrl cannot be changed
	 */
	public void setMailUrl(String mailUrl) {
		//this.mailUrl = mailUrl;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 * File system path to write mails in .txt files.
	 * Must be writable.
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
	 * the year to set
	 */
	public void setYear(String year) {
		if(year.matches("[\\d]{4}"))
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
	 * @deprecated
	 * baseUrl couldnot be changed.
	 * Fixed to  https://mail-archives.apache.org/mod_mbox/maven-users/
	 * 
	 */
	public void setBaseUrl(String baseUrl) {
		//this.baseUrl = baseUrl;
	}

	/**
	 * @param month
	 * the month to set
	 * 00 for all months 
	 * 01,02,03,...etc to retrieve  specific months.(01-12)
	 */
	public void setMonth(String month){
		if (month.matches("[\\d]{2},([\\d]{2})*$"))
			this.month = month;
		else
			this.month = "01,02,03,04,05,06,07,08,09,10,11,12";
		

	}
	/**
	 * 
	 * @return ConfigProperties 
	 * Reads cofig.properties file and  populate the values in ConfigProperties 
	 * If it fails to read the properties file then default values will be populated.
	 */
	public static ConfigProperties getInstance() {

		ConfigProperties configProperties = new ConfigProperties();
		FileInputStream inputStream = null;
		Properties properties = new Properties();
		try {
			inputStream = new FileInputStream("config.properties");
			properties.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found exception");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		configProperties.setMonth(properties.getProperty("month"));
		configProperties.setYear(properties.getProperty("year"));
		configProperties.setBaseUrl(properties.getProperty("baseUrl"));
		configProperties.setMailUrl(properties.getProperty("mailUrl"));
		configProperties.setPath(properties.getProperty("logpath"));
		return configProperties;

	}

}
