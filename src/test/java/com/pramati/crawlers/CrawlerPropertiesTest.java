package com.pramati.crawlers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pramati.crawlers.beans.CrawlerProperties;

public class CrawlerPropertiesTest {

	@Test
	public void testGetInstance() {
		CrawlerProperties crawlerProperties = CrawlerProperties.getInstance();
		assertSame(crawlerProperties, crawlerProperties);
	}
	

}
