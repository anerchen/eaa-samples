package eaa.chapter13.queryobject;

import java.util.List;

import eaa.chapter13.queryobject.activemapper.ActiveRecord;
import eaa.chapter13.queryobject.activemapper.Query;
import eaa.chapter13.queryobject.annotations.BelongsTo;
import eaa.chapter13.queryobject.annotations.MappedField;
import eaa.chapter13.queryobject.annotations.MappedTable;


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
	
	public static List<Bookmark> find( Query<Bookmark> query ) {
		return ActiveRecord.find( query, Bookmark.class );
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
