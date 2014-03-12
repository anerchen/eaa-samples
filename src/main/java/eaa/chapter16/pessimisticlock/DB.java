package eaa.chapter16.pessimisticlock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.ConcurrencyException;
import eaa.support.DatabaseServer;

public class DB {

	private static ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	private static ThreadLocal<String> userId = new ThreadLocal<String>();
 	
	public synchronized static void startTransaction( String user ) throws SQLException {
		userId.set( user );
		getConnection();
	}
	
	public synchronized static String getUserId() {
		System.err.println("asked for user ID " + userId.get() );
		return userId.get();
	}
	
	public synchronized static Connection getConnection() throws SQLException {
		if ( local.get() == null ) {
			Connection c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			local.set( c );
		}
		return local.get();
	}
	
	public static PreparedStatement prepare( String statement ) throws SQLException {
		return getConnection().prepareStatement( statement );
	}
	
	public synchronized static void rollback() {
		try {
			if ( local.get() != null ) {
				local.get().rollback();
				
				// using a new connection & but still need the userid
				ExclusiveReadLockManager.releaseSessionLocks( userId.get() );
				
				
				DatabaseServer.getInstance().releaseConnection( local.get() );
				local.set( null );
				userId.set("");
			}
		} catch ( Exception ex ) {
			System.err.println( ex.getMessage() );
		}
	}
	
	public synchronized static void commit() throws SQLException, ConcurrencyException {
		if ( local.get() != null ) {
				local.get().commit();
				
				ExclusiveReadLockManager.releaseSessionLocks( userId.get() );
				
				DatabaseServer.getInstance().releaseConnection( local.get() );
				local.set( null );
				userId.set("");
		} else {
			throw new ApplicationException("Could not commit - no transaction exists in this context.");
		}
	}
	
}
