package helper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	public static void crawlForLinks(HashSet<String> visited, Stack<String> tovisit, HashSet<String> matches,
			HashSet<String> srcDownloaded, String toPath, boolean quiet, boolean printCrawlList) {

		FileH.deleteDirectory(new File(toPath));
		// TODO parse JS loading src'es
		
		String format = "%-90s %5s %15s\n";
		long start = System.currentTimeMillis();
		while (!tovisit.isEmpty()) {
			String url;
			// Getting rid of already visited URL's, this can happen when one page links to
			// the same one multiple times.
			// Otherwise a stack.contains would have to be performed which takes too long.
			do {
				url = tovisit.pop();
			} while (visited.contains(url) && !tovisit.isEmpty());
			visited.add(url);
			try {
				Document doc = Jsoup.connect(url).get();
				URL aURL = new URL(url);
				String path = aURL.getFile();
				path = (path.equals("") ? "/index.html" : path);
				path = StringOp.urlFileNameSafe(path, true);
				path = toPath + path;

				Elements links = doc.select("a[href]");
				for (Element element : links) {
					String linkAbs = element.attr("abs:href");
					String linkAct = element.attr("href");
					boolean linkActIsSpecial = StringOp.linkActIsSpecial(linkAct);
					boolean circularLink = linkAbs.equals(url);
					if (!linkActIsSpecial && !linkAbs.equals("") && !circularLink) {
						String linkAbsMatch = linkAbs.toLowerCase().replace("http://", "").replace("https://", "");
						for (String match : matches) {
							if (linkAbsMatch.startsWith(match)) {
								if (!visited.contains(linkAbs)) {
									tovisit.push(linkAbs);
								}
							}
						}
					}
				}

				File file = new File(path);
				file.getParentFile().mkdirs();
				Elements linksHR = doc.select("a[href]");
				for (Element element : linksHR) {
					element.attr("href", StringOp.urlFileNameSafe(element.attr("href"), false));

				}
				try (PrintWriter out = new PrintWriter(path)) {
					out.println(doc.toString());
				}

				Elements srcs = doc.select("[src]");
				downloadResources(srcDownloaded, toPath, srcs, "src");

				Elements srccss = doc.select("link[href]");
				downloadResources(srcDownloaded, toPath, srccss, "href");

				if (!quiet) {
					System.out.format(format, "Checked URL " + StringOp.trimString(url, 78), tovisit.size() + " queued",
							visited.size() + " checked");
				}
			} catch (HttpStatusException e) {
				System.out.println(e.getMessage());
			} catch (UnsupportedMimeTypeException e) {
				// some kind of document, pdf, docx, etc
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(visited.size() + " elements in " + (end - start) + " ms");
		if (printCrawlList) {
			System.out.println(visited.toString());
		}
	}

	private static void downloadResources(HashSet<String> srcDownloaded, String toPath, Elements srcs, String access) {
		for (Element element : srcs) {
			if(srcDownloaded.add(element.attr(access))){
				try {
					String rel = element.attr(access);
					if(rel.startsWith("//")){
						rel = "https:"+rel;
					}
					if(rel.startsWith("http://")|| rel.startsWith("https://")) {
						URL aURL = new URL(rel);
						rel = aURL.getFile();
					}
					File fileS = new File(toPath+"/"+rel);
					fileS.getParentFile().mkdirs();
					
					String res = Network.downloadAndReturnString(element.attr("abs:"+access),toPath+"/"+StringOp.urlFileNameSafe(rel,true));
					
					downloadCSSResources(srcDownloaded, toPath, access, element, rel, res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
}

	private static void downloadCSSResources(HashSet<String> srcDownloaded, String toPath, String access,
			Element element, String rel, String res) throws Exception {
		if(res!=null) {
			String cssSrcRegex = "url\\(([^\\)]*)\\)";
			Matcher m = Pattern.compile(cssSrcRegex).matcher(res);
			while (m.find()) {
				String lnk = element.attr("abs:"+access);
				if(!srcDownloaded.contains(lnk)) {
					if(lnk.endsWith("/")) {
						lnk.substring(0,lnk.length()-1);
					}
					lnk = lnk.substring(0, lnk.lastIndexOf("/"));
					lnk = lnk+"/"+m.group(1);
					String newRel = StringOp.cutOffFileNameFromPath(rel);
					String savePath = toPath+"/"+newRel+"/"+m.group(1)+"/";
					File createFolder = new File(StringOp.cutOffFileNameFromPath(savePath));
					createFolder.getParentFile().mkdirs();
					Network.downloadAndReturnString(lnk,savePath);
					srcDownloaded.add(lnk);
				}
			}
		}
	}}
