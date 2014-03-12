package eaa.chapter12.foreignkeymapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import eaa.support.ApplicationException;

public class PersonMapper extends AbstractMapper<Person> {

	public static final String COLUMNS = " id, name ";
	public static final String TABLE = "people";
	
	private static final String UPDATE_STATEMENT = "UPDATE " + TABLE + " SET " +
			" name = ? WHERE id = ? ";
	
	protected String table() {
		return TABLE;
	}
	
	protected String findStatement() {
		return "SELECT " + COLUMNS +
		       " FROM " + TABLE + 
		       " WHERE id = ?";
	}
	
	protected String insertStatement() {
		return "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?,?)";
	}
	
	public Person find( long id ) {
		return abstractFind(id);
	}
	
	public List<Person> find() {
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
	
	public void update( Connection c, Person person ) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.prepareStatement( UPDATE_STATEMENT );
			
			stmt.setString( 1, person.getName() );
			stmt.setLong(   2, person.getId() );
			
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			release( stmt, rs );
		}
	}

	@Override
	protected Person doLoad(long id, ResultSet rs) throws SQLException {
		String name = rs.getString( 2 );
		
		return new Person( id, name );
	}
	
	protected void prepareInsert( PreparedStatement stmt, Person person ) throws SQLException {
		stmt.setLong( 1, person.getId() );
		stmt.setString( 2, person.getName() );
	}
	
}
