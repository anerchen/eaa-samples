package eaa.chapter10.activerecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

/**
 * This is sort of a hard-coded active record
 * a much better design is possible - this is an example
 */
public class Person {

	private long id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	
	private boolean isUpdated = false;
	
	public Person( String firstName, String lastName, String address, String city, String state, String zipCode ) {
		this( -1, firstName, lastName, address, city, state, zipCode );
	}
	
	private Person( long id, String firstName, String lastName, String address, String city, String state, String zipCode ) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;		
	}
		
	private void setUpdated() {
		isUpdated = true;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if ( ! this.address.equals( address ) ) {
			setUpdated();
		}
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if ( ! this.city.equals( city ) ) {
			setUpdated();
		}
		this.city = city;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if ( ! this.firstName.equals( firstName ) ) {
			setUpdated();
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if ( ! this.lastName.equals( lastName ) ) {
			setUpdated();
		}
		this.lastName = lastName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if ( ! this.state.equals( state ) ) {
			setUpdated();
		}
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		if ( ! this.zipCode.equals( zipCode ) ) {
			setUpdated();
		}
		this.zipCode = zipCode;
	}

	public long getId() {
		return id;
	}
	
	public void save() {
		// if new
		if ( id < 0 ) {
			insert();
		} else if ( isUpdated ) {
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
			
			stmt = c.prepareStatement( "INSERT INTO people (id,first_name,last_name,address,city,state,zip_code) " +
					                                     " VALUES (?,?,?,?,?,?,?) " );
			stmt.setLong( 1, getId() );
			stmt.setString( 2, getFirstName() );
			stmt.setString( 3, getLastName() );
			stmt.setString( 4, getAddress() );
			stmt.setString( 5, getCity() );
			stmt.setString( 6, getState() );
			stmt.setString( 7, getZipCode() );
			
			
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
			
			PreparedStatement stmt = c.prepareStatement( "UPDATE people set first_name=?,last_name=?,address=?,city=?,state=?,zip_code=? " +
					                                     " WHERE id=? " );
			stmt.setString( 1, getFirstName() );
			stmt.setString( 2, getLastName() );
			stmt.setString( 3, getAddress() );
			stmt.setString( 4, getCity() );
			stmt.setString( 5, getState() );
			stmt.setString( 6, getZipCode() );
			stmt.setLong( 7, getId() );
			
			stmt.executeUpdate();
			
			isUpdated = false;
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
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,first_name,last_name,address,city,state,zip_code FROM people WHERE id=?");
			stmt.setLong( 1, id );
			
			ResultSet results = stmt.executeQuery();
			
			Person person = null;
			if ( results.next() ) {
				person = new Person( 
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
	
	public static List<Person> find() {
		List<Person> rtnList = new ArrayList<Person>();
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,first_name,last_name,address,city,state,zip_code FROM people order by ID asc");
			
			ResultSet results = stmt.executeQuery();
			
			while( results.next() ) {
				rtnList.add(
						new Person( 
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
	
}
