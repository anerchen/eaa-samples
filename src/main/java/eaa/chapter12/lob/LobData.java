package eaa.chapter12.lob;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;

import eaa.support.ApplicationException;
import eaa.support.DatabaseServer;

public class LobData {
	
	private long id = -1;
	private LinkedHashSet<String> data = new LinkedHashSet<String>();
	
	public LobData() {
	}
	
	private LobData( long id, LinkedHashSet<String> data ) {
		this.id = id;
		this.data = data;
	}
	
	public long getId() {
		return id;
	}
	
	public HashSet<String> getDataSet() {
		return data;
	}

	private Blob prepareBlob() throws IOException, SQLException  {
		// prepare blob column - THESE ARE DATABASE DEPENDENT TYPES
		ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		ObjectOutputStream out = new ObjectOutputStream(bos) ;
	    out.writeObject(data);
	    out.flush();
	    byte[] bytes = bos.toByteArray();
	    out.close();
	    
	    return new org.hsqldb.jdbc.jdbcBlob( bytes );
	}
	
	public long insert() {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			Blob blob = prepareBlob();
			
			long id = KeyGenerator.getNextId( c, "lobs" );
			stmt = c.prepareStatement( "INSERT INTO lobs (id,data) VALUES(?,?)  ");
			stmt.setLong( 1, id );
			stmt.setBlob( 2,  blob );
			
			stmt.executeUpdate();
			
			return id;
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( stmt != null ) try { stmt.close(); } catch ( Exception ex ) {}
			DatabaseServer.getInstance().releaseConnection( c );
		}
	}
	
	public void update() {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			Blob blob = prepareBlob();
			
			stmt = c.prepareStatement( "UPDATE lobs set data=? where id=? ");
			stmt.setBlob( 1,  blob );
			stmt.setLong( 2, id );
			
			stmt.executeUpdate();
			
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( stmt != null ) try { stmt.close(); } catch ( Exception ex ) {}
			DatabaseServer.getInstance().releaseConnection( c );
		}		
	}
	
	public void destroy() {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			stmt = c.prepareStatement( "DELETE FROM lobs WHERE id=?");
			stmt.setLong( 1, id );
			
			stmt.executeUpdate();
			
		} catch ( Exception ex ) {
			try { c.rollback(); } catch ( Throwable t ) {}
			throw new ApplicationException( ex );
		} finally {
			if ( stmt != null ) try { stmt.close(); } catch ( Exception ex ) {}
			DatabaseServer.getInstance().releaseConnection( c );
		}			
	}
	
	@SuppressWarnings("unchecked")
	public static LobData find( long id ) {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet results = null;
		try {
			c = DatabaseServer.getInstance().getConnection();
			
			stmt = c.prepareStatement( "SELECT id,data FROM lobs WHERE id=?");
			stmt.setLong( 1, id );
			
			results = stmt.executeQuery();
			if ( results.next() ) {
				Blob blob = results.getBlob("data");
				
				System.out.println("*************************************");
				System.out.println( "DB READ OF ID=" + id );
				System.out.println(" RAW BLOB SIZE: " + blob.length() );
				System.out.println("*************************************");
				
				ObjectInputStream ois = new ObjectInputStream( blob.getBinaryStream() );
				
				Object o = ois.readObject();
				if ( o instanceof LinkedHashSet ) {
					LinkedHashSet<String> data = (LinkedHashSet<String>) o;
					LobData lobData = new LobData( id, data );
					return lobData;
				} else {
					return null;
				}	
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
	
	
}
