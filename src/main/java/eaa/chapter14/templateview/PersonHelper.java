package eaa.chapter14.templateview;

import java.io.Serializable;

import eaa.chapter14.pagecontroller.domain.Bookmark;
import eaa.chapter14.pagecontroller.domain.Person;

public class PersonHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Person person;
	
	public PersonHelper( Person person ) {
		this.person = person;
	}
	
	public String getBookmarksHTML() {
		StringBuffer out = new StringBuffer();
		
		out.append("<ul>");
		
		for( Bookmark bookmark : person.getBookmarks() ) {
			out.append("<li>");
			out.append("<a href=\"" + bookmark.getUrl() + "\">" );
			out.append( bookmark.getUrl() );
			out.append("</a>");
			out.append("</li>");
		}
		
		out.append("</ul>");
		
		return out.toString();
	}
	
}
