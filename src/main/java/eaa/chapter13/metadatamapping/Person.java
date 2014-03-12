package eaa.chapter13.metadatamapping;

import java.util.ArrayList;
import java.util.List;

import eaa.chapter13.metadatamapping.activemapper.ActiveRecord;
import eaa.chapter13.metadatamapping.annotations.HasMany;
import eaa.chapter13.metadatamapping.annotations.MappedField;
import eaa.chapter13.metadatamapping.annotations.MappedTable;

@MappedTable("people")
public class Person extends ActiveRecord {

	@MappedField("name")
	private String name;
	@MappedField("city")
	private String city;
	@MappedField("state")
	private String state;
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
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append( getClass().getCanonicalName() + " :: name=" + getName() + " city=" + getCity() + " state=" + getState() + " id=" + getId() );
		for( Bookmark m : getBookmarks() ) {
			b.append( "\n \\--> " + m.toString() );
		}
		return b.toString();
	}
	
}
