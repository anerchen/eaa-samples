package eaa.chapter11.identitymap;

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
	
	public abstract boolean equals( DomainObject o );
	
	public abstract AbstractMapper<DomainObject> getMapper();
	
	public abstract String toString();

}
