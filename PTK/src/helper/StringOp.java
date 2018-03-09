package helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class StringOp {
	public static String[] splitRecipient(String recipient) {
		int markerPos = recipient.indexOf(">");
		if (markerPos!=-1) {
			return new String[]{recipient.substring(0, markerPos),(recipient.substring(markerPos+1))};
		}
		return new String[] {recipient,recipient};
	}

	public static String injectVar(String target, String value, String placeholder) {
		return target.replaceAll(placeholder, value);
	}
	
	
	public static String cutOffFileNameFromPath(String path) {
		if(path.endsWith("/")) {
			path.substring(0,path.length()-1);
		}
		int endPos = path.lastIndexOf("/");
		return path.substring(0,endPos);
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
		Date date = new Date();		
		
		String trackingPixelHTML = "<img src=\""+trackingPixelURL+"?pxl=&id="+mailEncoded+"\" "
				+ "style=\"height:1px !important; width:1px !important; border: 0 !important; "
				+ "margin: 0 !important; padding: 0 !important\" width=\"1\" height=\"1\" border=\"0\">";

		cbody = StringOp.injectVar(cbody, name, "%name%");
		cbody = StringOp.injectVar(cbody, mailEncoded , "%mailaddress%");
		cbody = StringOp.injectVar(cbody, trackingPixelHTML , "%trackingpixel%");
		cbody = StringOp.injectVar(cbody, new SimpleDateFormat("yyyy").format(date) , "%yyyy%");
		cbody = StringOp.injectVar(cbody, new SimpleDateFormat("dd").format(date) , "%dd%");
		cbody = StringOp.injectVar(cbody, new SimpleDateFormat("MM").format(date) , "%MM%");
		
		return cbody;
	}
	
	public static String trimString(String s, int maxLength){
		if(s.length()>maxLength){
			return s.substring(0, Math.min(s.length(), maxLength-2))+"..";	
		}
		return s;
	}
	
	public static String urlFileNameSafe(String url, boolean rewriteHashBang) {
		url = url.replace("?", "-Q-");
		url = url.replace("&", "-A-");
		if(rewriteHashBang) {
			int hashTagPos = url.indexOf("#");
			if(hashTagPos!=-1) {
				url = url.substring(0,hashTagPos);			
			}	
		}
		return url;
	}
	
	public static String urlFileNameSafeRevert(String filename) {
		filename = filename.replace("-Q-", "?");
		filename = filename.replace("-A-", "&");
		filename = filename.replace("-H-", "#");
		return filename;
	}
	
	public static boolean linkActIsSpecial(String linkAct) {
		return (
				linkAct.startsWith("javascript:") 
				|| linkAct.startsWith("#") 
				|| linkAct.startsWith("mailto:") 
				|| linkAct.startsWith("tel:") 
				|| linkAct.startsWith("skype:")
		);
	}


	public static String resolveDotComponents(String aa) {
		String res = "";
		List<String> a = Arrays.asList(aa.split("/"));
		for (String string : a) {
			if(string.equals("..")) {
				res = cutOffFileNameFromPath(res);
			}else {
				res += "/"+string;
			}
		}
		return res.substring(1);
	}
}
