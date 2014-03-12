package eaa.chapter11.lazyload.lazyinitialization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

// incomplete object - just for loading
public class Bookmark {

	private long id;
	private Person person;
	private String url;
	
	public Bookmark() {
		
	}
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
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



	public static List<Bookmark> findByPerson( Person p ) {
		System.out.println(" Loading bookmarks for " + p.getName() + " ("+ p.getId() + ")" );
		Connection c = null;
		List<Bookmark> bookmarks = new ArrayList<Bookmark>();
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,person_id,url FROM bookmarks WHERE person_id=?");
			stmt.setLong( 1, p.getId() );
			
			ResultSet results = stmt.executeQuery();
			
			while( results.next() ) {
				Bookmark m = new Bookmark();
				m.id = results.getLong(1);
				m.person = p;
				m.url = results.getString( 3 );
				
				bookmarks.add( m );
			}
			
			
			c.rollback(); // can always rollback - never updates
			return bookmarks;
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}
	}
	
}
