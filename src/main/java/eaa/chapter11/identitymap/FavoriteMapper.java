package eaa.chapter11.identitymap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import eaa.support.ApplicationException;

public class FavoriteMapper extends AbstractMapper<Favorite> {

	public static final String COLUMNS = " id, url, visits ";
	public static final String TABLE = "favorites";
	
	private static final String UPDATE_STATEMENT = "UPDATE " + TABLE + " SET " +
			" url = ?, visits = ? WHERE id = ? ";
	
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
	
	public Favorite find( long id ) {
		return abstractFind(id);
	}
	
	public List<Favorite> find() {
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
	
	public void update( Connection c, Favorite favorite ) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.prepareStatement( UPDATE_STATEMENT );
			
			stmt.setString( 1, favorite.getUrl() );
			stmt.setInt(    2, favorite.getVisits() );
			stmt.setLong(   3, favorite.getId() );
			
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			release( stmt, rs );
		}
	}

	@Override
	protected Favorite doLoad(long id, ResultSet rs) throws SQLException {
		String url = rs.getString( 2 );
		int visits = rs.getInt( 3 );
		
		return new Favorite( id, url, visits );
	}
	
	protected void prepareInsert( PreparedStatement stmt, Favorite favorite ) throws SQLException {
		stmt.setLong( 1, favorite.getId() );
		stmt.setString( 2, favorite.getUrl() );
		stmt.setInt( 3, favorite.getVisits() );
	}
	
}
