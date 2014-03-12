package eaa.chapter12.associationtablemapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;


// incomplete object - just for loading
public class Skill  {

	private long id;
	private String name;
	
	public Skill( long id, String name ) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
	public String toString() {
		return "skill=" + name + " (" + id + ")";
	}
	
	public Skill find( long id ) {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			PreparedStatement stmt = c.prepareStatement( "SELECT id,name FROM skills WHERE id=?");
			stmt.setLong( 1, id );
			
			ResultSet results = stmt.executeQuery();
			
			Skill skill = null;
			if ( results.next() ) {
				skill = new Skill( 
							results.getLong( 1 ),
							results.getString( 2 )
						);
			} else {
				throw new ApplicationException("Record not found");
			}
			
			c.rollback(); // can always rollback - never updates
			return skill;
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}		
	}
	
}
