package eaa.chapter12.foreignkeymapping;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class WorkManager {

	private Set<DomainObject> newObjects = new HashSet<DomainObject>();
	private Set<DomainObject> dirtyObjects = new HashSet<DomainObject>();
	private Set<DomainObject> deletedObjects = new HashSet<DomainObject>();
	
	private String[] tableOrder = { "people", "bookmarks" };
	
	public void registerNew( DomainObject obj ) {
		if ( obj.getId() > 0 ) { 
			throw new ApplicationException("Object is not new (id > 0): " + obj.toString() );
		}
		newObjects.add( obj );
	}

	public void registerDirty( DomainObject obj ) {
		if ( obj.getId() <= 0 ) {
			throw new ApplicationException("Object is new (id <= 0): " + obj.toString() );
		}
		dirtyObjects.add( obj );
	}
	
	public void registerDeleted( DomainObject obj ) {
		if ( newObjects.remove(obj) ) return;
		dirtyObjects.remove(obj);
		deletedObjects.add( obj );
	}
	
	public void registerClean( DomainObject obj ) {
		if ( obj.getId() <= 0 ) {
			throw new ApplicationException("Object should be registered new (id <= 0): " + obj.toString() );
		}
	}
	
	public void commit() {
		Connection c = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			c.setAutoCommit( false );
			System.out.println("*****BEGIN*****");
			
			insertNew( c );
			updateDirty( c );
			deleteRemoved( c );
			
			c.commit();
			System.out.println("*****COMMIT*****");
		} catch ( SQLException ex ) {
			System.out.println("*****ROLLBACK*****");
			try { c.rollback(); } catch ( Exception ex2 ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( c != null ) {
				DatabaseServer.getInstance().releaseConnection( c );
			}
		}
	}
	
	private void insertNew( Connection c ) {
		Set<DomainObject> processed = new HashSet<DomainObject>();
		for( String tableName : tableOrder ) {
			for( DomainObject o : newObjects ) {
				if ( o.getMapper().table().equalsIgnoreCase( tableName ) ) {
					if ( ! processed.contains( o ) ) {
						processed.add( o );
						o.getMapper().insert( c, o );
						System.out.println("INSERTED: " + o.toString() );
					}
				}
			}
		}
	}
	
	private void updateDirty( Connection c ) {
		Set<DomainObject> processed = new HashSet<DomainObject>();
		for( String tableName : tableOrder ) {
			for( DomainObject o : dirtyObjects ) {
				if ( o.getMapper().table().equalsIgnoreCase( tableName ) ) {
					if ( ! processed.contains( o ) ) {
						processed.add( o );
						o.getMapper().update( c, o );
						System.out.println("UPDATED: " + o.toString() );
					}
				}
			}
		}
	
	}
	
	private void deleteRemoved( Connection c ) {
		List<String> order = new ArrayList<String>();
		for( String s : tableOrder ) order.add(s);
		Set<DomainObject> processed = new HashSet<DomainObject>();
		
		for( String tableName : order ) {
			for( DomainObject o : deletedObjects ) {
				if ( o.getMapper().table().equalsIgnoreCase( tableName ) ) {
					if ( ! processed.contains( o ) ) {
						processed.add( o );
						o.getMapper().destroy( c, o );
						System.out.println("UPDATED: " + o.toString() );
					}
				}
			}
		}
	}
	
}
