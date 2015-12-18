package com.pramati.crawlers;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.pramati.crawlers.beans.Utils;
import java.nio.file.InvalidPathException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class UtilsTest {

	@Test(expected = IOException.class)
	public void testCreateDirectory() throws IOException {
		Assert.assertTrue(Utils.createDirectory("/home3/iva"));
	}

	@Test(expected = InvalidPathException.class)
	public void testPrepareTargetUrlWithEmtpy() {
		Utils.prepareTargetUrls("", "", "");
	}

	@Test
	public void testReplaceFileSystemChars(){
		String path = Utils.replaceFileSystemChars("siva/ganesh\\");
	//	System.out.println(path);
		String regEx = "\\/";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(path);
		Assert.assertFalse(matcher.lookingAt());
		
	}
	@Test
	public void normalizeSubjectTest(){
		String subject = Utils.nomalizeSubject("Re : Sobha");
		//System.out.println(subject);
		String regEx = "(\\s*re\\s*:\\s*)|(\\s*RE\\s*:\\s*)|(\\s*Re\\s*:\\s*)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(subject);
		Assert.assertFalse(matcher.lookingAt());
	}
}
