package com.vishnu.springmongo.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "commits")
public class Commit {

	private String id;
	private String revisionNumber;
	private String author;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRevisionNumber() {
		return revisionNumber;
	}
	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}


}
