package ptk;

import java.util.ArrayList;

public class PhishedRecipient {
	private String recipient;
	private String ip;
	private String datetime;
	private int hits;
	
	public PhishedRecipient(String recipient, String ip, String datetime) {
		this.recipient=recipient;
		this.ip=ip;
		this.datetime=datetime;
		this.hits=1;
	}
	public void anotherHit() {
		hits++;
	}
	public String getRecipient() {
		return recipient;
	}
	public String getIP() {
		return ip;
	}
	public String getDateTime() {
		return datetime;
	}
	public int getHits() {
		return hits;
	}
	/**
	 * @param phishedRecipients containing a list of phsied recipients
	 * @param recipient email to find
	 * @return returns the index of the specified recipient in the list. returns -1 if it cannot be found.
	 */
	public static int phishedRecipientsIndexOf(ArrayList<PhishedRecipient> phishedRecipients, String recipient) {
		int index = -1;
		for (PhishedRecipient phishedRecipient : phishedRecipients) {
			index++;
			if (phishedRecipient.getRecipient().equals(recipient)) {
				return index;
			}
		}
		return -1;
	}
}