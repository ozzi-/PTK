package ptk;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import helper.Mail;
import helper.Network;
import helper.Printing;
import helper.StringOp;

public class Operation {
	static void mEvaluate(boolean full, Properties config, List<String> toList) {
		ArrayList<PhishedRecipient> phishedRecipients = new ArrayList<PhishedRecipient>();
		int uniqueHits = 0;
		String logURL = config.getProperty("logURL");
		System.out.println("Getting Log from "+logURL);
		String log = Network.doGet(logURL);
		
		if (log == null || log.equals("")) {
			System.out.println("Log is empty or unavailable");
		}else {
			JsonArray logEntries = null;
			try {
				logEntries = Json.parse(log).asArray();
			} catch (Exception e) {
				System.out.println("Can't parse log.json - "+e.getMessage()+" - Aborting");
				System.exit(-1);
			}
			for (String recipient : toList) {
				String mail = StringOp.splitRecipient(recipient)[0];
				for (JsonValue jv : logEntries) {
					if (jv.asObject().get("id").asString().equals(mail)) {
						uniqueHits = updatePhishes(phishedRecipients, uniqueHits, mail, jv);
					}
				}
			}
			System.out.println(uniqueHits + " of " + toList.size() + " read the mail / clicked the link.");
			if (full) {
				Printing.printPhisedFull(phishedRecipients);
			} else {
				Printing.printSimple(phishedRecipients);
			}
		}
	}

	private static int updatePhishes(ArrayList<PhishedRecipient> phishedRecipients, int uniqueHits, String recipient, JsonValue jv) {
		boolean hit = jv.asObject().get("type").asString().equals("hit");
		int index = PhishedRecipient.phishedRecipientsIndexOf(phishedRecipients, recipient);
		if (index>=0) {
			if(hit) {
				phishedRecipients.get(index).anotherHit();				
			}else {
				phishedRecipients.get(index).anotherRead();
			}
		} else {
			PhishedRecipient pr = new PhishedRecipient(
					recipient,
					jv.asObject().get("ip").asString(),
					jv.asObject().get("datetime").asString(),
					hit?1:0,
					hit?0:1
			);
			phishedRecipients.add(pr);
			uniqueHits++;
		}
		return uniqueHits;
	}

	static void mSimSend(List<String> toList, String body, String trackingPixelURL) throws UnsupportedEncodingException {
		for (String recipient : toList) {
			String cbody = StringOp.injectBody(recipient,body,trackingPixelURL);			
			System.out.println("Sending Mail to " + recipient + " with body:");
			System.out.println(cbody);
			System.out.println("----");
		}
	}

	static void mSend(String smtpHost, int smtpPort, String smtpUser, String smtpPassword, String from, String subject, List<String> toList, String body, String trackingPixelURL) throws UnsupportedEncodingException {
		System.out.println("Preparing to send to "+toList.size()+" recipients");
		for (String recipient : toList) {
			String cbody = StringOp.injectBody(recipient,body,trackingPixelURL);
			String mailAddress = StringOp.splitRecipient(recipient)[0];
			boolean success = Mail.sendSimpleMailSMTP(smtpHost, smtpPort, smtpUser, smtpPassword, from, mailAddress, subject, cbody);
			System.out.println((success ? "Successfully" : "Unsuccessfully") + " sent mail to " + mailAddress);
		}
	}
}
