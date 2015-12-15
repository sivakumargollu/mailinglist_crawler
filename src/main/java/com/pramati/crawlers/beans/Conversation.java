package com.pramati.crawlers.beans;

import java.util.HashMap;
import java.util.HashSet;

public class Conversation {
	
	private String month = "";
	private String year = "";
	private String subject = "";
	private HashSet<Mail> mails = new HashSet<Mail>();
	
	
	
	
	
	
	
	
	/**
	 * @return the mails
	 */
	public HashSet<Mail> getMails() {
		return mails;
	}
	/**
	 * @param mails the mails to set
	 */
	public void setMails(HashSet<Mail> mails) {
		this.mails = mails;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conversation other = (Conversation) obj;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	
	
}
