package eaa.chapter12.foreignkeymapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class KeyGenerator {
	
	public synchronized static long getNextId( Connection c, String table ) {
		PreparedStatement stmt = null;
		ResultSet results = null;
		
		long nextValue = 0;
		try {
			// PESSIMISTIC - ONLINE LOCK
			stmt = c.prepareStatement("SELECT next_id FROM app_keys WHERE table_name=?");
			stmt.setString( 1, table );
			results = stmt.executeQuery();
			results.next();
			nextValue = results.getLong(1);
			
			
			// OPTIMISTIC OFFLINE LOCK
			stmt = c.prepareStatement("UPDATE app_keys SET next_id=? WHERE table_name=? and next_id=?");
			stmt.setLong( 1, nextValue + 1 );
			stmt.setString( 2, table );
			stmt.setLong( 3, nextValue );
			stmt.executeUpdate();
		} catch ( SQLException ex) {
			throw new ApplicationException(ex);
		} finally {
			if ( results != null) try { results.close(); } catch ( Exception ex2 ) {}
 			if ( stmt != null) try { stmt.close(); } catch ( Exception ex2 ) {}
		}
		
		return nextValue;
	}

	public synchronized static long getNextId( String table ) {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		

		long nextValue = 0;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			
			// PESSIMISTIC - ONLINE LOCK
			stmt = c.prepareStatement("SELECT next_id FROM app_keys WHERE table_name=? FOR UPDATE");
			stmt.setString( 1, table );
			results = stmt.executeQuery();
			results.next();
			nextValue = results.getLong(1);
			
			// cleanup
			results.close();
			stmt.close();
			
			// OPTIMISTIC OFFLINE LOCK
			stmt = c.prepareStatement("UPDATE app_keys SET next_id=? WHERE table_name=? and next_id=?");
			stmt.setLong( 1, nextValue + 1 );
			stmt.setString( 2, table );
			stmt.setLong( 3, nextValue );
			stmt.executeUpdate();
			
			c.commit();
 		} catch ( SQLException ex ) {
 			try {
 				c.rollback();
 				throw new ApplicationException( "Couldn't get next identity value for table '"+table+"'.");
 			} catch ( Exception ex2 ) {}
 		} finally {
 			if ( results != null) try { results.close(); } catch ( Exception ex2 ) {}
 			if ( stmt != null) try { stmt.close(); } catch ( Exception ex2 ) {}
 			if ( c != null ) DatabaseServer.getInstance().releaseConnection(c);
 		}
		
 		return nextValue;
	}
	
}
