package eaa.chapter12.singletableinheritance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public abstract class Player {
	
	protected long id = -1;
	private String name = null;
	
	public Player() {
	}
	
	protected Player( long id, String name ) {
		this.id = id;
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
	protected abstract void load( ResultSet rs ) throws SQLException;
	protected abstract void update( Connection c ) throws SQLException;
	 
	public long insert() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
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
			
			stmt = c.prepareStatement( "DELETE FROM players WHERE id=?");
			stmt.setLong( 1, id );
			
			stmt.executeUpdate();
			
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
			
			stmt = c.prepareStatement( "SELECT * FROM players WHERE id=?");
			stmt.setLong( 1, id );
			
			results = stmt.executeQuery();
			if ( results.next() ) {
				String className = results.getString("class");
				
				Class<Player> clazz =  (Class<Player>) Player.class.getClassLoader().loadClass( className );
				
				Player p = clazz.newInstance();
				p.id = id;
				p.name = results.getString( "name" );
				p.load( results );
				
				return p;
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
