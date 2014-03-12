package eaa.chapter15.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class PersonDTO implements Serializable {

	private static final long serialVersionUID = 4489885785558524264L;
	  
	private long id;
	private String name;
	private ArrayList<String> bookmarks = new ArrayList<String>();
	
	public ArrayList<String> getBookmarks() {
		return bookmarks;
	}
	public void setBookmarks(ArrayList<String> bookmarks) {
		this.bookmarks = bookmarks;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
