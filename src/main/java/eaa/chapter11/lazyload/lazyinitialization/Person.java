package eaa.chapter11.lazyload.lazyinitialization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class Person {

	private long id;
	private String name;
	private List<Bookmark> bookmarks = new ArrayList<Bookmark>();
	private boolean bookmarksLoaded = false;
	
	public Person( long id, String name ) {
		this.id = id;
		this.name = name;
	}
	
	public Person( String name ) {
		this.id = -1;
		this.name = name;
		this.bookmarksLoaded = true;
	}
	
	
	public List<Bookmark> getBookmarks() {
		if ( ! bookmarksLoaded ) {
			bookmarks = Bookmark.findByPerson( this );
			bookmarksLoaded = true;
		}
		
		return bookmarks;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void save() {
		// if new
		if ( id < 0 ) {
			insert();
		} else {
			update();
		}
		
	}
	
	public void destroy() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( true );
			
			PreparedStatement stmt = c.prepareStatement("DELETE FROM people where id=?");
			stmt.setLong( 1, getId() );
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}	
	}
	
	private void insert() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( true );
			
			PreparedStatement stmt = c.prepareStatement("SELECT MAX(id) FROM people");
			ResultSet result = stmt.executeQuery();
			result.next();
			this.id = result.getLong( 1 ) + 1;	
			
			stmt = c.prepareStatement( "INSERT INTO people (id,name) " +
					                                     " VALUES (?,?) " );
			stmt.setLong( 1, getId() );
			stmt.setString( 2, getName() );
			
			
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}			
	}
	
	private void update() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( true );
			
			PreparedStatement stmt = c.prepareStatement( "UPDATE people set name=? " +
					                                     " WHERE id=? " );
			stmt.setString( 1, getName() );
			stmt.setLong( 2, getId() );
			
			stmt.executeUpdate();
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}			
	}
	
	public static Person find( long id ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,name FROM people WHERE id=?");
			stmt.setLong( 1, id );
			
			ResultSet results = stmt.executeQuery();
			
			Person person = null;
			if ( results.next() ) {
				person = new Person( 
						results.getLong( 1 ),
						results.getString( 2 )
						);
			} else {
				throw new ApplicationException("Record not found");
			}
			
			c.rollback(); // can always rollback - never updates
			return person;
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}		
	}
	
}
