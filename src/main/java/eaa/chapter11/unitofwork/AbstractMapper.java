package eaa.chapter11.unitofwork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public abstract class AbstractMapper<E extends DomainObject> {

	protected abstract String findStatement();
	
	protected Connection openConnection() throws SQLException {
		return DatabaseServer.getInstance().getConnection();
	}
	protected void releaseConnection( Connection c, PreparedStatement stmt, ResultSet rs )  {
		release( stmt, rs );
		DatabaseServer.getInstance().releaseConnection( c );
	}
	
	protected void release( PreparedStatement stmt, ResultSet rs )  {
		if ( rs != null ) {
			try { rs.close(); } catch ( Exception ex ) {}
		}
		if ( stmt != null ) {
			try { stmt.close(); } catch ( Exception ex ) {}
		}
	}
	
	protected E abstractFind( long id ) {
		E result = null;
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			c = openConnection();
			c.setAutoCommit( true );
			
			stmt = c.prepareStatement( findStatement() );
			stmt.setLong( 1, id );
			rs = stmt.executeQuery();
			rs.next();
			
			result = load( rs );
			
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			releaseConnection( c, stmt, rs );
		}	
	
		return result;
	}
	
	protected List<E> loadAll( ResultSet rs ) throws SQLException {
		List<E> results = new ArrayList<E>();
		
		while( rs.next() ) {
			results.add( load( rs ) );
		}
		
		return results;
	}
	
	protected E load( ResultSet rs ) throws SQLException {
		long id = rs.getLong( 1 ); // Notice - this forces the ID column to be first in the select
		E result = doLoad( id, rs );
		
		return result;
	}
	
	protected abstract E doLoad( long id, ResultSet rs ) throws SQLException;
	
	protected long findNextId( Connection c ) throws SQLException {
		PreparedStatement stmt = c.prepareStatement( " SELECT MAX(id) FROM " + table() );
		ResultSet rs = stmt.executeQuery();
		long id = 1;
		if ( rs.next() ) {
			id = rs.getLong(1) + 1;
		}
		rs.close();
		stmt.close();
		return id;
	}
	
	public void destroy( Connection c,  E subject ) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.prepareStatement( " DELETE FROM " + table() + " WHERE id = ? " );
			stmt.setLong( 1, subject.getId() );
			stmt.executeUpdate();
			
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			release( stmt, rs );
		}			
	}
	
	public long insert( Connection c, E subject ) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			subject.setId( findNextId( c ) );
			
			stmt = c.prepareStatement( insertStatement() );
			prepareInsert( stmt, subject );
			stmt.execute();
			
			return subject.getId();
		} catch ( SQLException ex ) {
			throw new ApplicationException( ex );
		} finally {
			release( stmt, rs );
		}	
	}
	
	protected abstract String table();
	protected abstract String insertStatement();
	protected abstract void prepareInsert( PreparedStatement stmt, E subject ) throws SQLException ;
	
	public abstract void update( Connection c, E favorite );
	
}
