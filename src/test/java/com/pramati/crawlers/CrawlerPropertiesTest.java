package com.pramati.crawlers;

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.pramati.crawlers.beans.CrawlerProperties;

public class CrawlerPropertiesTest {

	@Test
	public void testGetInstance() {
		CrawlerProperties crawlerProperties = CrawlerProperties.getInstance();
		Logger logger = Logger.getLogger(CrawlerProperties.class.getName());
		logger.log(Level.INFO,"Processing!!!");
		logger.log(Level.INFO,crawlerProperties.toString());
		assertEquals(crawlerProperties, crawlerProperties);
	}
	

}
