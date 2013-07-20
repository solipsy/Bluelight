package parser;

import java.util.Date;

public class Post {
	private String poster,body, url, category, postNumber, threadTitle;
	private Date postDate;

	public Post(String poster, String body, Date postDate, String url, String category) {
		this.poster = poster;
		this.body = body;
		this.postDate = postDate;
		this.url = url;
	}
	
	public Post () {
		poster = body = url = category = "";
		postDate = new Date();
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(String postNumber) {
		this.postNumber = postNumber;
	}

	public String getThreadTitle() {
		return threadTitle;
	}

	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}
	
	
	
	

}
