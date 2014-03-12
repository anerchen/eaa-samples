package eaa.chapter12.concretetableinheritance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

/**
 * This model of Class-Table Inheritence
 * only works with 1 level of inheritance unless you
 * put super.load() calls in the 3rd and below levels
 * @author mhelmick
 *
 */
public abstract class Player {
	
	protected long id = -1;
	private String name = null;
	
	public Player() {
	}
	
	protected Player( long id, String name ) {
		this.id = id;
		this.name = name;
	}
	
	protected Player( String name ) {
		this.id = -1;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	protected abstract void save( Connection c ) throws SQLException;
	protected abstract void load( Connection c ) throws SQLException;
	protected abstract void update( Connection c ) throws SQLException;
	protected abstract void delete( Connection c ) throws SQLException;
	 
	public long insert() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			this.id = KeyGenerator.getNextId( c, "players" );
			save( c );
			
			c.commit();
			return id;
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			DatabaseServer.getInstance().releaseConnection( c );
		}
	}
	
	public void update() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();

			update( c );
			
			c.commit();
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			DatabaseServer.getInstance().releaseConnection( c );
		}		
	}
	
	public void destroy() {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			delete( c );
			
			c.commit();
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( stmt != null ) try { stmt.close(); } catch ( Exception ex ) {}
			DatabaseServer.getInstance().releaseConnection( c );
		}			
	}
	
	@SuppressWarnings("unchecked")
	public static Player find( long id ) {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			Player p = null;
			
			try {
				p = new BaseballPlayer();
				p.id = id;
				p.load( c );
				return p;
			} catch ( Exception ex ) {
			}
			
			try {
				p = new FootballPlayer();
				p.id = id;
				p.load( c );
				return p;
			} catch ( Exception ex ) {	
			}
			
			throw new ApplicationException("ID " + id + " was not found.");
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( stmt != null ) try { stmt.close(); } catch ( Exception ex ) {}
			if ( results != null ) try { stmt.close(); } catch ( Exception ex ) {}
			DatabaseServer.getInstance().releaseConnection( c );
		}			
	}
	
	public abstract String toString();
	
}
