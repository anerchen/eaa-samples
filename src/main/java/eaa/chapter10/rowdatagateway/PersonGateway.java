package eaa.chapter10.rowdatagateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class PersonGateway implements Person {

	private long id;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	
	public PersonGateway( long id, String firstName, String lastName, String address, String city, String state, String zipCode ) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	
	public PersonGateway( String firstName, String lastName, String address, String city, String state, String zipCode ) {
		this.id = -1;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getAddress()
	 */
	public String getAddress() {
		return address;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#setAddress(java.lang.String)
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getCity()
	 */
	public String getCity() {
		return city;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#setCity(java.lang.String)
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getFirstName()
	 */
	public String getFirstName() {
		return firstName;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#setFirstName(java.lang.String)
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getId()
	 */
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getLastName()
	 */
	public String getLastName() {
		return lastName;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#setLastName(java.lang.String)
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getState()
	 */
	public String getState() {
		return state;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#setState(java.lang.String)
	 */
	public void setState(String state) {
		this.state = state;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#getZipCode()
	 */
	public String getZipCode() {
		return zipCode;
	}
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#setZipCode(java.lang.String)
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#insert()
	 */
	public void insert() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( true );
			
			PreparedStatement stmt = c.prepareStatement("SELECT MAX(id) FROM people");
			ResultSet result = stmt.executeQuery();
			result.next();
			setId( result.getLong( 1 ) + 1 );	
			
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
	
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#update()
	 */
	public void update() {
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
			
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see eaa.chapter10.rowdatagateway.Person#delete()
	 */
	public void delete() {
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
	
	
}
