package linkcollector;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import parser.Detector;

public class LinkProvider {
	private static ArrayList<String[]> linkies;
	
	
	public static ArrayList<String[]> getLinks (String URL, String category, int limit) {
		linkies = new ArrayList<String[]> ();
		Document articleElement = null;
		String pagination = null;
		int pagesNr = 1;
		int currentPage = 1;
		boolean hasMore = false;
		int counter = 0;
		
		do {
			try {
				articleElement = (Document) Jsoup.connect(URL + "/page" + currentPage + "?pp=40&sort=lastpost&order=desc&daysprune=-1").timeout(20000).get(); //
//				articleElement = Jsoup.parse(html);
//				System.out.println (articleElement.html());
				
				if (articleElement.select("div.windowbg").last().select("span").first() != null){
					pagination = articleElement.select("div.windowbg").last().select("span").first().text();
					pagesNr = Integer.parseInt (pagination.split(" ")[pagination.split(" ").length - 1]);
				}
				else pagesNr = 1;
				
				System.out.println ("PAGE: " + currentPage + " / " + pagesNr + " : " + pagination);
				
				Elements links = articleElement.select("a[href^=threads]"); 
				
				boolean show = true;
				for (Element link : links) {
					if (show) {
						System.out.println(link.attr("href").split("\\?")[0]);
						String [] linky = new String [] {link.attr("href").split("\\?")[0], category};
						linkies.add(linky);
//						counter ++;
					}
					show = !show;
				}
				
				if (currentPage == pagesNr) hasMore = false;
				else {
					currentPage++;
					hasMore = true;
				}
				
				Thread.currentThread().sleep(200);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		while (hasMore && counter < limit);
		
		return linkies;
	}

}
