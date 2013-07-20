package parser;

import index.SolrSrvr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BluelightParser {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	private static Collection<SolrInputDocument> docs;
	private static SolrSrvr solr;
	static int currentPage;
	
	public static void parseURL (String URL, Detector syn, String category) {   

		try {
			

			
			String threadTitle = null;
			String pagination = null;
			int pagesNr = 1;
			currentPage = 1;
			int postNumber = 0;
			boolean hasMore = false;
			do {
				try {
					ArrayList<String> posts = new ArrayList<String>();
					ArrayList<Date> postDates = new ArrayList<Date>();
					ArrayList<String> postPeople = new ArrayList<String>();
					docs = new ArrayList<SolrInputDocument>();
					System.out.println ("PAGE: " + currentPage + " URL: " + URL);
					Document articleElement = null;
					articleElement = (Document) Jsoup.connect(URL + "/page" + currentPage).timeout(20000).get();
					
					threadTitle = articleElement.select("div.titlebg").text().toLowerCase().replaceAll("\\p{Punct}", " ");
					threadTitle = threadTitle.replaceAll("thread", "");
					
					if (articleElement.select("div.windowbg").first().select("span").first() != null){
						pagination = articleElement.select("div.windowbg").first().select("span").first().text();
						pagesNr = Integer.parseInt (pagination.split(" ")[pagination.split(" ").length - 1]);
					}
					else pagesNr = 1;
					
					Elements postsList = articleElement.select("div.windowbg")
							.select("div:not(div[align=right])")
							.select("div:not(div.quote)");
					postsList.select("div.quote").remove();
					postsList.select("div[style^=margin]").remove();

					for (Element e : postsList) {
						String body = e.text().toLowerCase().replaceAll("\\p{Punct}", " ");
						
						if (!body.equals("reply") &&  !(body.length() < 4)) {
							if (!body.substring(0,4).equals("page")) {
								posts.add(body);
//								System.out.println ("BODY: " + body);
//								System.out.println(e.outerHtml() + "\n----------\n");
							}
						}	
					}


					Calendar cal = Calendar.getInstance();
					Date preIndexDate = null;
					
					Elements timesList = new Elements(articleElement.select("div.Oldwindowbg2")); 
					Elements peopleList = new Elements(timesList.select("a[href^=members]"));
					timesList.select("a[href^=members]").remove();
					for (Element e : timesList) {
						String time = e.text().substring(6, e.text().length());
						if (time.equals("Today")) {
							preIndexDate = new Date();
						}
						else if (time.equals("Yesterday")) {
							preIndexDate = new Date();
							cal.setTime(preIndexDate);
							cal.add (Calendar.DAY_OF_MONTH, -1);
							preIndexDate = cal.getTime();
						}
						else {
							try {
								preIndexDate = sdf.parse(time);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						}
						postDates.add(preIndexDate);
					}
					
					for (Element e : peopleList) {
						if (!e.text().equals("")) {
							postPeople.add(e.text());;
						}
					}
					if (currentPage == pagesNr) hasMore = false;
					else {
						currentPage++;
						hasMore = true;
					}
					
					int min1 = Math.min(postPeople.size(),posts.size());
					int min2 = Math.min(postDates.size(),posts.size());
					int looplength = Math.min(min1, min2);
					
//					System.out.println (postPeople.size());
//					System.out.println (postDates.size());
//					System.out.println (posts.size());
					
					for (int i = 0; i < looplength; i++) {
						Post post = new Post();
						post.setBody(posts.get(i));
						post.setCategory(category);
						post.setPoster(postPeople.get(i));
						post.setPostDate(solr.getIndexedDate(postDates.get(i)));
						post.setPostNumber(i + "");
						post.setUrl(URL + "/" + currentPage);
						post.setThreadTitle(threadTitle);
						docs.add(solr.convertToSolrDoc(post));
					}
					Thread.currentThread().sleep(200);
					

					
					if (docs.size() > 0 && docs != null) {
						solr.addPosts(docs);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			while (hasMore);
			
			
		} catch (Exception e) {
			System.out.println ("Exception at URL: " + URL + " page: " + currentPage);
		}
		
		if (docs.size() > 0 && docs != null) solr.commit();
	}
	

	
	

}
