package eaa.chapter12.foreignkeymapping;


// incomplete object - just for loading
public class Bookmark extends DomainObject {

	private Person person;
	private String url;
	
	private static BookmarkMapper manager = new BookmarkMapper();
	
	public Bookmark( long id, Person person, String url ) {
		super(id);
		this.person = person;
		this.url = url;
	}
	
	public Bookmark( String url ) {
		super();
		this.person = null;
		this.url = url;
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

	@Override
	public boolean equals(DomainObject o) {
		return this.getId() == o.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractMapper getMapper() {
		return manager;
	}

	@Override
	public String toString() {
		return "url='" + getUrl() + "' id=" + getId() + " person_id=" + getPerson().getId();
	}


	
}
