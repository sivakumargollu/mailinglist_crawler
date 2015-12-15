package com.pramati.crawlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

	private String year = "2014";
	private String month = "00";
	private String baseUrl  = "";
	private String path = "/home/sivag/logs";
	
	
	

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
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

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * @param month
	 * the month to set
	 */
	public void setMonth(String month){
		if (month.matches("[\\d]{2},[\\d]{2}$"))
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
		
		configProperties.setMonth(properties.get("month").toString());
		configProperties.setYear(properties.get("year").toString());
		configProperties.setBaseUrl(properties.getProperty("baseUrl"));
		return configProperties;

	}

}
