package crtl;

import index.SolrSrvr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import linkcollector.LinkProvider;

import network.Net;
import network.TalkEdge;

import org.apache.solr.common.SolrInputDocument;

import parser.BluelightParser;
import parser.Detector;
import parser.Post;

public class Launcher {
	private static Collection<SolrInputDocument> docs;
	private static SolrSrvr solr;
	private static Detector detector;
	private static Net net;

	public static void main(String args[]) {
		Detector syn = new Detector();
		solr = new SolrSrvr();
//		solr.deleteIndex();
		getLinksAndCrawl();

//		crawl("http://www.bluelight.ru/vb/threads/667854-CD-Social-Information-booth-VS-Whose-got-the-lighter/page4", "");
		
	}
	//http://www.bluelight.ru/vb/forums/48-Psychedelic-Drugs
	//http://www.bluelight.ru/vb/forums/58-Other-Drugs
	//	http://www.bluelight.ru/vb/forums/70-Cannabis-Discussion
	
	private static void getLinksAndCrawl () {
		ArrayList<String[]> linkies = LinkProvider.getLinks("http://www.bluelight.ru/vb/forums/27-Drugs-in-the-Media", "media", 50); 
				
		int c = 0;
		for (String[] linky : linkies) {
			if (c>7) crawl ("http://www.bluelight.ru/vb/" + linky[0], linky[1]);
			c++;
		}
	}
	
	private static void crawl (String URL, String category) {
		BluelightParser.parseURL(URL, detector, category);
	}
	

	
	

	
	
}
