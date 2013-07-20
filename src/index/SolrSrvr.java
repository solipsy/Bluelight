package index;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import analysis.ISO8601CanonicalDateFormat;

import parser.Post;

public class SolrSrvr {
	private final String url0 = "http://localhost:8983/solr/";
	private static int nrID = 0;
	private int ID;
	private static ConcurrentUpdateSolrServer server0;
	private static int nrIndexed;

	public SolrSrvr() {
		server0 = new ConcurrentUpdateSolrServer(url0, 1000, 2);
		ID = 0;
		nrIndexed = 0;
	}

	public void deleteIndex() {
		try {
			server0.deleteByQuery("*:*");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized SolrInputDocument convertToSolrDoc(Post c) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", c.getUrl() + "$" + c.getPoster() + "$"
				+ c.getPostDate().toString() + "$"
				+ c.getPostNumber());
		doc.addField("author", c.getPoster());
		doc.addField("body", c.getBody());
		doc.addField("category", c.getCategory());
		doc.addField("date", c.getPostDate());
		doc.addField("title", c.getThreadTitle());
		// System.out.println ("doc ok");
		return doc;
	}



	public static synchronized void addPosts(Collection<SolrInputDocument> docs) {
		try {
			UpdateResponse add = server0.add(docs);
			// UpdateResponse commit = server.commit();
			nrIndexed += docs.size();
			System.out.println("total indexed docs: " + nrIndexed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void commit() {
		try {
			server0.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Post> getPostsByDate(Date dateFrom, Date dateTo, String q) {
		ArrayList<Post> result = new ArrayList<Post>();
		SolrQuery query = new SolrQuery();

		query.setQuery("body:" + q + " AND date:[" + ISO8601CanonicalDateFormat.get().format(dateFrom)+ " TO " +  ISO8601CanonicalDateFormat.get().format(dateTo) + "]");
		query.addSortField("date", SolrQuery.ORDER.asc);
		query.setStart(0);
		QueryResponse rsp = new QueryResponse();
		query.setRows(1000);
		try {
			rsp = server0.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		int indexSize = (int) rsp.getResults().getNumFound();
		System.out.println("SOLR response: " + rsp.getResults().getNumFound());
		for (int i = 0; i < indexSize / 32000 + 1; i++) {
			query.setStart(i * 32000);
			query.setRows(32000);
			try {
				rsp = server0.query(query);
			} catch (SolrServerException e) {
				e.printStackTrace();
			}
			SolrDocumentList docs = rsp.getResults();
			for (SolrDocument doc : docs) {
				Post post = getPostFromDoc(doc);
				result.add(post);
			}
		}
		return result;
	}

	public ArrayList<Post> getPosts(String category) {
		ArrayList<Post> result = new ArrayList<Post>();
		SolrQuery query = new SolrQuery();
		query.setQuery("category:" + category);
		query.addSortField("id", SolrQuery.ORDER.asc);
		query.setStart(0);
		QueryResponse rsp = new QueryResponse();
		query.setRows(1000);
		try {
			rsp = server0.query(query);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		int indexSize = (int) rsp.getResults().getNumFound();
		System.out.println("SOLR response: " + rsp.getResults().getNumFound());
		for (int i = 0; i < indexSize / 32000 + 1; i++) {
			query.setStart(i * 32000);
			query.setRows(32000);
			try {
				rsp = server0.query(query);
			} catch (SolrServerException e) {
				e.printStackTrace();
			}
			SolrDocumentList docs = rsp.getResults();
			for (SolrDocument doc : docs) {
				Post post = getPostFromDoc(doc);
				result.add(post);
			}
		}
		System.out.println(result.size());
		return result;
	}

	private Post getPostFromDoc(SolrDocument doc) {
		Post post = new Post();
		post.setPoster((String) doc.get("author"));
		post.setBody((String) doc.get("body"));
		post.setCategory((String) doc.get("category"));
		post.setPostDate((Date) doc.get("date"));
		post.setUrl((String) doc.get("url"));
		post.setThreadTitle((String) doc.get("title")); 
		return post;
	}

	public int getNrIdx() {
		return nrIndexed;
	}

	public static Date getIndexedDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date indexFormatDate = null;
		try {
			indexFormatDate = sdf.parse(sdf.format(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return indexFormatDate;
	}

}
