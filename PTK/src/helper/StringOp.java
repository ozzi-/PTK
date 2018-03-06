package helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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
	
	public static String injectBody(String recipient, String cbody, String trackingPixelURL) {
		String emailAddress = StringOp.splitRecipient(recipient)[0];
		String name = StringOp.splitRecipient(recipient)[1];
		String mailEncoded = StringOp.urlEncodeString(emailAddress);
		String trackingPixelHTML = "<img src=\""+trackingPixelURL+"?pxl=&id="+mailEncoded+"\" style=\"height:1px !important; width:1px !important; border: 0 !important; margin: 0 !important; padding: 0 !important\" width=\"1\" height=\"1\" border=\"0\">";

		cbody = StringOp.injectVar(cbody, name, "%name%");
		cbody = StringOp.injectVar(cbody, mailEncoded , "%mailaddress%");
		cbody = StringOp.injectVar(cbody, trackingPixelHTML , "%trackingpixel%");
		
		Date date = new Date();		
		cbody = StringOp.injectVar(cbody, new SimpleDateFormat("yyyy").format(date) , "%yyyy%");
		cbody = StringOp.injectVar(cbody, new SimpleDateFormat("dd").format(date) , "%dd%");
		cbody = StringOp.injectVar(cbody, new SimpleDateFormat("MM").format(date) , "%MM%");
		
		return cbody;
	}
}
