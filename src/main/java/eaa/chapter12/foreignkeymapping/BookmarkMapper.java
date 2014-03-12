package eaa.chapter12.foreignkeymapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class BookmarkMapper extends AbstractMapper<Bookmark> {

	public static final String COLUMNS = " id, person_id, url ";
	public static final String TABLE = "bookmarks";
	
	private static final String UPDATE_STATEMENT = "UPDATE " + TABLE + " SET " +
			" person_id = ?, url = ? WHERE id = ? ";
	
	protected String table() {
		return TABLE;
	}
	
	protected String findStatement() {
		return "SELECT " + COLUMNS +
		       " FROM " + TABLE + 
		       " WHERE id = ?";
	}
	
	protected String insertStatement() {
		return "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?,?,?)";
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
				long id = results.getLong(1);
				// don't need person ID
				String url = results.getString( 3 );
				
				Bookmark m = new Bookmark( id, p, url );
				
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
	
	public Bookmark find( long id ) {
		return abstractFind(id);
	}
	
	public List<Bookmark> find() {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			c = openConnection();
			
			stmt = c.prepareStatement( "SELECT " + COLUMNS + " FROM " + TABLE );
			rs = stmt.executeQuery();
			
			return loadAll( rs );
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			releaseConnection( c, stmt, rs );
		}
	}
	
	public void update( Connection c, Bookmark bookmark ) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.prepareStatement( UPDATE_STATEMENT );
			
			stmt.setLong( 1, bookmark.getPerson().getId() );
			stmt.setString( 2, bookmark.getUrl() );
			stmt.setLong( 3, bookmark.getId() );
			
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			release( stmt, rs );
		}
	}

	@Override
	protected Bookmark doLoad(long id, ResultSet rs) throws SQLException {
		String url = rs.getString( 3 );
		
		return new Bookmark( id, null, url );
	}
	
	protected void prepareInsert( PreparedStatement stmt, Bookmark bookmark ) throws SQLException {
		stmt.setLong( 1, bookmark.getId() );
		stmt.setLong( 2, bookmark.getPerson().getId() );
		stmt.setString( 3, bookmark.getUrl() );
		
	}
	
}
