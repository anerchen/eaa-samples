package eaa.chapter12.foreignkeymapping;

import java.sql.Connection;

public abstract class DomainObject {
	
	private long id = -1;
	
	public DomainObject() {
		id = -1;
	}
	
	public DomainObject( long id ) {
		this.id = id;
	}

	public final long getId() {
		return id;
	}

	public final void setId(long id) {
		this.id = id;
	}
	
	public long nextId( Connection c ) {
		return KeyGenerator.getNextId( c, getMapper().table() );
	}
	
	public abstract boolean equals( DomainObject o );
	
	public abstract AbstractMapper<DomainObject> getMapper();
	
	public abstract String toString();

}
