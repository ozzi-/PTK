package helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class StringOp {
	public static String[] splitRecipient(String recipient) {
		if (recipient.contains(">")) {
			int markerPos = recipient.indexOf(">");
			return new String[]{recipient.substring(0, markerPos),(recipient.substring(markerPos+1))};
		}
		return new String[] {recipient,recipient};
	}

	public static String injectVar(String target, String value, String placeholder) {
		return target.replaceAll(placeholder, value);
	}
	
	public static String urlEncodeString(String string) {
		try {
			string =  URLEncoder.encode(new String(Base64.getEncoder().encode(string.getBytes())), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("This should not happen - Aborting");
			System.exit(-1);
		}
		return string;
	}
	
	public static String injectBody(String recipient, String cbody) {
		String emailAddress = StringOp.splitRecipient(recipient)[0];
		String name = StringOp.splitRecipient(recipient)[1];
		String mailEncoded = StringOp.urlEncodeString(emailAddress);

		cbody = StringOp.injectVar(cbody, name, "%name%");
		cbody = StringOp.injectVar(cbody, mailEncoded , "%mailaddress%");
		return cbody;
	}
}
