package eaa.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DB {

	private static ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	private static ThreadLocal<String> userId = new ThreadLocal<String>();
 	
	public synchronized static void startTransaction( String user ) throws SQLException {
		getConnection();
		userId.set( user );
	}
	
	public synchronized static String getUserId() {
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
				DatabaseServer.getInstance().releaseConnection( local.get() );
				local.set( null );
				userId.set("");
			}
		} catch ( Exception ex ) {
		}
	}
	
	public synchronized static void commit() throws SQLException {
		if ( local.get() != null ) {
				local.get().commit();
				DatabaseServer.getInstance().releaseConnection( local.get() );
				local.set( null );
				userId.set("");
		} else {
			throw new ApplicationException("Could not commit - no transaction exists in this context.");
		}
	}
	
}
