package eaa.chapter14.pagecontroller.domain;

import java.util.List;

import activemapper.ActiveRecord;
import activemapper.BelongsTo;
import activemapper.MappedField;
import activemapper.MappedTable;

@MappedTable("bookmarks")
public class Bookmark extends ActiveRecord {

	@BelongsTo("Person")
	private Person person;
	@MappedField("url")
	private String url;
	
	public Bookmark() {
		super();
	}
	
	public Bookmark( Person person, String url ) {
		super();
		this.person = person;
		this.url = url;
	}
	
	public static Bookmark find( long id ) {
		return ActiveRecord.find( Bookmark.class, id );
	}
	
	public static List<Bookmark> find( String conditions, Object...params) {
		return ActiveRecord.find( Bookmark.class, conditions, params ); 
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString() {
		return getClass().getCanonicalName() + " :: " + url + " id=" + getId();
	}

}
