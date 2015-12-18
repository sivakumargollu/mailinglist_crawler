package com.pramati.crawlers;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.pramati.crawlers.beans.CrawlerProperties;

public class TestPrasing {

	public static void main(String args[]) throws IOException {
		//String s = Jsoup.parse("Raise an issue in JIRA.&#010;&#010;On Mar 26, 2010, at 5:58 AM, wytten wrote:&#010;&#010;&gt; &#010;&gt; I've got an issue related to password decryption in 3.0-alpha7 that makes the&#010;&gt; mvn -X option effectively unusable.  The password I've got in my&#010;&gt; settings.xml file looks like this:&#010;&gt; &#010;&gt;            &lt;password&gt;{DESede}y+qq...==&lt;/password&gt;&#010;&gt; &#010;&gt; This is an Artifactory setup password and it does work, however mvn -X logs&#010;&gt; exceptions about it so frequently that it makes -X almost impossible to use. &#010;&gt; Is there some way I can suppress this behavior through configuration?  The&#010;&gt; exception that it logs over and over again is:&#010;&gt; &#010;&gt; [DEBUG] Failed to decrypt password for server central:&#010;&gt; org.sonatype.plexus.components.cipher.PlexusCipherException:&#010;&gt; java.lang.ArrayIndexOutOfBoundsException&#010;&gt; org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException:&#010;&gt; org.sonatype.plexus.components.cipher.PlexusCipherException:&#010;&gt; java.lang.ArrayIndexOutOfBoundsException&#010;&gt; ...&#010;&gt; Caused by: java.lang.ArrayIndexOutOfBoundsException&#010;&gt; &#009;at java.lang.System.arraycopy(Native Method)&#010;&gt; &#009;at&#010;&gt; org.sonatype.plexus.components.cipher.PBECipher.decrypt64(PBECipher.java:175)&#010;&gt; &#009;... 47 more&#010;&gt; &#010;&gt; &#010;&gt; &#010;&gt; -- &#010;&gt; View this message in context: http://old.nabble.com/3.0-alpha7-password-decryption-tp28042090p28042090.html&#010;&gt; Sent from the Maven - Users mailing list archive at Nabble.com.&#010;&gt; &#010;&gt; &#010;&gt; ---------------------------------------------------------------------&#010;&gt; To unsubscribe, e-mail: users-unsubscribe@maven.apache.org&#010;&gt; For additional commands, e-mail: users-help@maven.apache.org&#010;&gt; &#010;&#010;Thanks,&#010;&#010;Jason&#010;&#010;----------------------------------------------------------&#010;Jason van Zyl&#010;Founder,  Apache Maven&#010;http://twitter.com/jvanzyl&#010;----------------------------------------------------------&#010;&#010;&#010;").text();
		//System.out.println(s);
		CrawlerProperties crawlerProperties = CrawlerProperties.getInstance();
		System.out.println(crawlerProperties.getBaseUrl());
		System.out.println(crawlerProperties.getMonth());
		System.out.println(crawlerProperties.getYear());
	}
}
