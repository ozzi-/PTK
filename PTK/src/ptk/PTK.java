package ptk;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import helper.File;
import helper.Printing;

public class PTK {
	public static void main(String[] args) throws UnsupportedEncodingException {
		Properties config = File.loadProperties();
		String smtpHost = config.getProperty("smtpHost");
		int smtpPort = Integer.valueOf(config.getProperty("smtpPort"));
		String smtpUser = config.getProperty("smtpUser");
		String smtpPassword = config.getProperty("smtpPassword");
		String from = config.getProperty("from");
		String subject = config.getProperty("subject");
		String trackingPixelURL = config.getProperty("trackingPixelURL");
		String recipientFile = config.getProperty("recipientFile");
		List<String> toList = File.readFileAsArray(recipientFile);
		String bodyFile = config.getProperty("bodyFile");
		String body = File.readFileAsString(bodyFile);
		
		args[0]="evaluatefull";
		
		if (args.length == 0) {
			Printing.printUsage();
		} else {
			switch (args[0]) {
			case "simsend":
				Operation.mSimSend(toList, body,trackingPixelURL);
				break;
			case "send":
				Operation.mSend(smtpHost, smtpPort, smtpUser, smtpPassword, from, subject, toList, body, trackingPixelURL);
				break;
			case "evaluate":
				Operation.mEvaluate(false, config, toList);
				break;
			case "evaluatefull":
				Operation.mEvaluate(true, config, toList);
				break;
			default:
				Printing.printUsage();
				break;
			}
		}
	}
}
