package eaa.chapter16.pessimisticlock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import eaa.support.ConcurrencyException;
import eaa.support.DatabaseServer;

public class ExclusiveReadLockManager {

	private static final String INSERT_SQL = "INSERT INTO locks ( locked_table, locked_id, owner_id ) VALUES ( ?,?,? )";
	private static final String DELETE_SINGLE_SQL = "DELETE FROM locks WHERE locked_table=? and locked_id=? and owner_id=? ";
	private static final String DELETE_ALL_SQL = "DELETE FROM locks WHERE owner_id=?";
	private static final String CHECK_SQL = "SELECT count(*) FROM locks WHERE locked_table=? and locked_id=? and owner_id=?";
	
	private static Map<String,String> lockWaits = java.util.Collections.synchronizedMap( new HashMap<String,String>() );
	
	private static boolean hasLock( String table, long id, String owner ) {
		boolean hasLock = false;
		
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( true );
			
			stmt = c.prepareStatement( CHECK_SQL );
			stmt.setString( 1, table );
			stmt.setLong( 2, id );
			stmt.setString( 3, owner );
			
			results = stmt.executeQuery();
			results.next();
			long count = results.getLong(1);
			hasLock = count > 0;
			
			
		} catch ( SQLException ex ) {
		} finally {
			if ( results != null ) { try { results.close(); } catch ( Exception ex2 ) {} }
			if ( stmt != null ) { try { stmt.close(); } catch ( Exception ex2 ) {} }
			if ( c != null ) {
				try { DatabaseServer.getInstance().releaseConnection( c ); } catch ( Exception ex2 ) {}
			}
		}
		
		return hasLock;
	}
	
	public synchronized static void verifyLock( String table, long id, String owner ) throws ConcurrencyException {
		if ( ! hasLock( table, id, owner ) ) {
			throw new ConcurrencyException("Users '" + owner + "' does not have a lock on table '" + table + "' for record '" + id + "'." );
		}
		System.out.println("LOCK MANAGER: verified lock for: " + table + "," + id + "," + owner );
	}
	
	public synchronized static void acquireLock( String table, long id, String owner ) throws ConcurrencyException {
		if ( ! hasLock( table, id, owner ) ) {
			
			while( lockWaits.get(table + "_" + id ) != null ) {
				try {
					lockWaits.get( table+ " " + id ).wait();
				} catch ( InterruptedException ex ) {}
			}
			
			Connection c = null;
			PreparedStatement stmt = null;
			ResultSet results = null;
			try {
				c = DatabaseServer.getInstance().getConnection();
				c.setAutoCommit( true );
				
				stmt = c.prepareStatement(INSERT_SQL);
				stmt.setString( 1, table );
				stmt.setLong( 2, id );
				stmt.setString( 3, owner );
				int rows = stmt.executeUpdate();
				
				if ( rows == 0 ) {
					throw new SQLException("lock row not inserted");
				}
				
				lockWaits.put( table + "_" + id, table + "_" + id );
				System.out.println("LOCK MANAGER: granted lock for: " + table + "," + id + "," + owner );
			} catch ( SQLException ex ) {
				System.out.println("LOCK MANAGER: REJECTING LOCK REQUEST for: " + table + "," + id + "," + owner );
				throw new ConcurrencyException("unable obtain lock for id " + id + " on table " + table + ".", ex );
			} finally {
				if ( results != null ) { try { results.close(); } catch ( Exception ex2 ) {} }
				if ( stmt != null ) { try { stmt.close(); } catch ( Exception ex2 ) {} }
				if ( c != null ) {
					try { DatabaseServer.getInstance().releaseConnection( c ); } catch ( Exception ex2 ) {}
				}				
			}
		}
	}
	
	public synchronized static void releaseLock( String table, long id, String owner ) throws ConcurrencyException {
		if ( ! hasLock( table, id, owner ) ) {
			Connection c = null;
			PreparedStatement stmt = null;
			ResultSet results = null;
			try {
				c = DatabaseServer.getInstance().getConnection();
				c.setAutoCommit( true );
				
				stmt = c.prepareStatement(DELETE_SINGLE_SQL);
				stmt.setString( 1, table );
				stmt.setLong( 2, id );
				stmt.setString( 3, owner );
				stmt.executeUpdate();
				
				String waitObj = lockWaits.remove( table + "_" + id );
				if ( waitObj != null ) {
					waitObj.notifyAll();
				}
				
				System.out.println("LOCK MANAGER: released lock for: " + table + "," + id + "," + owner );
			} catch ( SQLException ex ) {
				System.out.println("LOCK MANAGER: unable to relase lock for: " + table + "," + id + "," + owner );
				throw new ConcurrencyException("unable release lock for id " + id + " on table " + table + ".", ex );
			} finally {
				if ( results != null ) { try { results.close(); } catch ( Exception ex2 ) {} }
				if ( stmt != null ) { try { stmt.close(); } catch ( Exception ex2 ) {} }
				if ( c != null ) {
					try { DatabaseServer.getInstance().releaseConnection( c ); } catch ( Exception ex2 ) {}
				}				
			}
		}		
	}
	
	public synchronized static void releaseSessionLocks( String owner ) throws ConcurrencyException {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( true );
			
			stmt = c.prepareStatement(DELETE_ALL_SQL);
			stmt.setString( 1, owner );
			stmt.executeUpdate();
			
			System.out.println("LOCK MANAGER: released session locks for: " + owner );
		} catch ( SQLException ex ) {
			System.out.println("LOCK MANAGER: unable to relase lock for: " + owner );
			throw new ConcurrencyException("unable release all locks for user " + owner, ex );
		} finally {
			if ( results != null ) { try { results.close(); } catch ( Exception ex2 ) {} }
			if ( stmt != null ) { try { stmt.close(); } catch ( Exception ex2 ) {} }
			if ( c != null ) {
				try { DatabaseServer.getInstance().releaseConnection( c ); } catch ( Exception ex2 ) {}
			}				
		}
			
	}
	
	
	
}
