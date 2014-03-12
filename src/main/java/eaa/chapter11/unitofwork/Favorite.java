package eaa.chapter11.unitofwork;

public class Favorite extends DomainObject {

	private String url;
	private int visits;
	
	private static FavoriteMapper manager = new FavoriteMapper();
	
	public Favorite( long id, String url, int visits ) {
		super( id );
		this.url = url;
		this.visits = visits;
	}
	
	public Favorite( String url, int visits ) {
		super();
		this.url = url;
		this.visits = visits;
	}
	
	@SuppressWarnings("unchecked")
	public AbstractMapper getMapper() {
		return manager;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}

	@Override
	public boolean equals(DomainObject o) {
		Long a = this.getId();
		Long b = o.getId();
		
		return a.equals(b);
	}
	
	public String toString() {
		return this.getClass().getName() + " id=" + getId() + " url=" + getUrl() + " visits=" + visits;
	}

}
