package com.pramati.crawlers;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.jsoup.Connection;

import com.pramati.crawlers.beans.Conversation;

public class Crawler extends Crawlable {

	CountDownLatch latch = null;

	public void crawl() {
		for (int pageno = 0; pageno < pagesCount; pageno++) {
			Connection.Response response = this.getResponse(this
					.appendPageno(pageno));
			this.setConversations(response.body());
		}
	}

	public void run() {
		boolean countDown = false;
		try {
			this.crawl();
			latch.countDown();
			System.out.println("Latch count is" + latch.getCount());
		} catch (Exception e) {
			e.printStackTrace();
			if (!countDown)
				latch.countDown();
		} finally {
			if (!countDown)
				latch.countDown();
		}

	}

	/**
	 * 
	 * @param pageno
	 * @return String Url like
	 *         "http://mail-archives.apache.org/mod_mbox/maven-users//201101.mbox/ajax/thread"
	 *         will converted to
	 *         "http://mail-archives.apache.org/mod_mbox/maven-users//201101.mbox/ajax/thread?1"
	 */
	public String appendPageno(int pageno) {
		return url.concat("?" + pageno);
	}
}
