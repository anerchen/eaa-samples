package eaa.chapter10.tabledatagateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class PersonGateway {

	public static ResultSet findById( long personId ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,first_name,last_name,address,city,state,zip_code FROM people where id=?");
			stmt.setLong( 1, personId );
			
			ResultSet results = stmt.executeQuery();
			
			c.rollback(); // can always rollback - never updates
			
			return results;
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}
	}
	
	public static ResultSet findByLastName( String lastName ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement("SELECT id,first_name,last_name,address,city,state,zip_code FROM people where last_name = ?");
			stmt.setString( 1, lastName );
			
			ResultSet results = stmt.executeQuery();
			
			c.rollback(); // can always rollback - never updates
			
			return results;
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}		
	}
	
	public static void insertPerson( String first_name, String last_name, String address, String city, String state, String zip_code ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement("SELECT MAX(id) FROM people");
			ResultSet result = stmt.executeQuery();
			result.next();
			long nextId = result.getLong( 1 ) + 1;	
			
			stmt = c.prepareStatement( "INSERT INTO people (id,first_name,last_name,address,city,state,zip_code) " +
					                                     " VALUES (?,?,?,?,?,?,?) " );
			stmt.setLong( 1, nextId );
			stmt.setString( 2, first_name );
			stmt.setString( 3, last_name );
			stmt.setString( 4, address );
			stmt.setString( 5, city );
			stmt.setString( 6, state );
			stmt.setString( 7, zip_code );
			
			stmt.executeUpdate();
			
			c.commit(); // can always rollback - never updates
		} catch ( SQLException ex ) {
			try { c.rollback(); } catch ( Exception ex2 ) { }
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}		
	}
	
	public static void deletePerson( long personId ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement("DELETE FROM people where id=?");
			stmt.setLong( 1, personId );
			stmt.executeUpdate();
			
			c.commit(); // can always rollback - never updates
		} catch ( SQLException ex ) {
			try { c.rollback(); } catch ( Exception ex2 ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}		
	}
	
}
