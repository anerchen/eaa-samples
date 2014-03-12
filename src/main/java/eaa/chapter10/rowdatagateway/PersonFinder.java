package eaa.chapter10.rowdatagateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class PersonFinder {

	public static List<Person> findAll() {
		List<Person> rtnList = new ArrayList<Person>();
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,first_name,last_name,address,city,state,zip_code FROM people order by ID asc");
			
			ResultSet results = stmt.executeQuery();
			
			while( results.next() ) {
				rtnList.add(
						new PersonGateway( 
								results.getLong( 1 ),
								results.getString( 2 ),
								results.getString( 3 ),
								results.getString( 4 ),
								results.getString( 5 ),
								results.getString( 6 ),
								results.getString( 7 )
								) );
			}
			
			c.rollback(); // can always rollback - never updates
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}
		return rtnList;
	}
	
	public static Person findById( long id ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,first_name,last_name,address,city,state,zip_code FROM people WHERE id=?");
			stmt.setLong( 1, id );
			
			ResultSet results = stmt.executeQuery();
			
			Person person = null;
			if ( results.next() ) {
				person = new PersonGateway( 
						results.getLong( 1 ),
						results.getString( 2 ),
						results.getString( 3 ),
						results.getString( 4 ),
						results.getString( 5 ),
						results.getString( 6 ),
						results.getString( 7 )
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

