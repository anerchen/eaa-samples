package eaa.chapter10.datamapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import eaa.support.ApplicationException;

public class PersonMapper extends AbstractMapper<Person> {

	public static final String COLUMNS = " id, first_name, last_name, address, city, state, zip_code ";
	public static final String TABLE = "people";
	
	private static final String UPDATE_STATEMENT = "UPDATE " + TABLE + " SET " +
			" first_name = ?, last_name = ?, address = ?, city = ?, state = ?, zip_code =? WHERE id = ? ";
	
	protected String table() {
		return TABLE;
	}
	
	protected String findStatement() {
		return "SELECT " + COLUMNS +
		       " FROM " + TABLE + 
		       " WHERE id = ?";
	}
	
	protected String insertStatement() {
		return "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?,?,?,?,?,?,?)";
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
	
	public void update( Person p ) {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			c = openConnection();
			
			stmt = c.prepareStatement( UPDATE_STATEMENT );
			
			stmt.setString( 1, p.getFirstName() );
			stmt.setString( 2, p.getLastName() );
			stmt.setString( 3, p.getAddress() );
			stmt.setString( 4, p.getCity() );
			stmt.setString( 5, p.getState() );
			stmt.setString( 6, p.getZipCode() );
			stmt.setLong(   7, p.getId() );
			
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			releaseConnection( c, stmt, rs );
		}
	}

	@Override
	protected Person doLoad(long id, ResultSet rs) throws SQLException {
		String firstName = rs.getString( 2 );
		String lastName = rs.getString( 3 );
		String address = rs.getString( 4 );
		String city = rs.getString( 5 );
		String state = rs.getString( 6 );
		String zipCode = rs.getString( 7 );
		
		return new Person( id, firstName, lastName, address, city, state, zipCode );
	}
	
	protected void prepareInsert( PreparedStatement stmt, Person p ) throws SQLException {
		stmt.setLong( 1, p.getId() );
		stmt.setString( 2, p.getFirstName() );
		stmt.setString( 3, p.getLastName() );
		stmt.setString( 4, p.getAddress() );
		stmt.setString( 5, p.getCity() );
		stmt.setString( 6, p.getState() );
		stmt.setString( 7, p.getZipCode() );
	}
	
}
