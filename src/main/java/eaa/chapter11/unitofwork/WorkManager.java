package eaa.chapter11.unitofwork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class WorkManager {

	private Set<DomainObject> newObjects = new HashSet<DomainObject>();
	private Set<DomainObject> dirtyObjects = new HashSet<DomainObject>();
	private Set<DomainObject> deletedObjects = new HashSet<DomainObject>();
	
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
		for( DomainObject o : newObjects ) {
			o.getMapper().insert( c, o );
			System.out.println("INSERTED: " + o.toString() );
		}
	}
	
	private void updateDirty( Connection c ) {
		for( DomainObject o : dirtyObjects ) {
			o.getMapper().update( c, o );
			System.out.println(" UPDATED: " + o.toString() );
		}
	}
	
	private void deleteRemoved( Connection c ) {
		for( DomainObject o : deletedObjects ) {
			o.getMapper().destroy( c, o );
			System.out.println(" DELETED: " + o.toString() );
		}
	}
	
}
