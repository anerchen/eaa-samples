package eaa.chapter15.server.domain;

import java.util.ArrayList;
import java.util.List;

import activemapper.ActiveRecord;
import activemapper.HasMany;
import activemapper.MappedField;
import activemapper.MappedTable;


@MappedTable("people")
public class Person extends ActiveRecord {

	@MappedField("name")
	private String name;
	@HasMany(listClass="Bookmark",identityColumn="person_id")
	private List<Bookmark> bookmarks = new ArrayList<Bookmark>();
	
	public Person() {
		super();
	}
	
	public Person( String name ) {
		super();
		this.name = name;
	}
	
	@SuppressWarnings("unchecked")
	public static Person find( long id ) {
		return ActiveRecord.find( Person.class, id );
	}
	
	public static List<Person> find( String conditions, Object...params) {
		return ActiveRecord.find( Person.class, conditions, params ); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append( getClass().getCanonicalName() + " :: name=" + getName() + " id=" + getId() );
		for( Bookmark m : getBookmarks() ) {
			b.append( "\n \\--> " + m.toString() );
		}
		return b.toString();
	}
	
}
