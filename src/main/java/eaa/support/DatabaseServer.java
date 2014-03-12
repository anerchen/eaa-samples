package eaa.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class DatabaseServer {

	private static DatabaseServer instance = null;
	
	private Queue<Connection> connection_queue = new LinkedList<Connection>();
	
	public static DatabaseServer getInstance() {
		if ( instance == null ) {
			instance = new DatabaseServer();
		}
		return instance;
	}
	
	public synchronized Connection getConnection() throws SQLException {
		if ( connection_queue.size() == 0 ) {
			openConnection();
		}
		return connection_queue.remove();
	}
	

	
	public synchronized void releaseConnection( Connection c ) {
		try {
			c.rollback();
			connection_queue.add( c );
		} catch ( SQLException ex ) {
			System.err.println("leaked connection " + c );
			System.err.println( ex.getMessage() );
			ex.printStackTrace( System.err );
		}
	}
	
	private void openConnection() throws SQLException {
		try {
	        Class.forName("org.hsqldb.jdbcDriver" );
	    } catch (Exception e) {
	        System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
	        e.printStackTrace();
	        return;
	    }

	    Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
	    connection_queue.add( c );
	}
	
}
