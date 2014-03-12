package eaa.chapter12.foreignkeymapping;

import java.util.ArrayList;
import java.util.List;

public class Person extends DomainObject {

	private String name;
	private List<Bookmark> bookmarks = new ArrayList<Bookmark>();
	private boolean bookmarksLoaded = false;
	
	private static PersonMapper manager = new PersonMapper();
	
	public Person( long id, String name ) {
		super(id);
		this.name = name;
	}
	
	public Person( String name ) {
		super();
		this.name = name;
		this.bookmarksLoaded = true;
	}
	
	
	public List<Bookmark> getBookmarks() {
		if ( ! bookmarksLoaded ) {
			bookmarks = BookmarkMapper.findByPerson( this );
			bookmarksLoaded = true;
		}
		
		return bookmarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "name='" + getName() + "' id=" + getId();
	}
	
}
