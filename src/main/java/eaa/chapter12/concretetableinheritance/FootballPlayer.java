package eaa.chapter12.concretetableinheritance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FootballPlayer extends Player {

	private long receptions;
	
	public FootballPlayer() {
		super();
	}
	
	public FootballPlayer( String name, long receptions ) {
		super( name );
		this.receptions = receptions;
	}
	
	public long getReceptions() {
		return receptions;
	}

	public void setReceptions(long receptions) {
		this.receptions = receptions;
	}

	@Override
	protected void load( Connection c ) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("SELECT * FROM con_football_players WHERE id=?");
		stmt.setLong( 1, getId() );
		ResultSet results = stmt.executeQuery();
		results.next();
		this.setName( results.getString( "name" ) );
		this.setReceptions( results.getLong( "receptions" ) );
		results.close();
		stmt.close();
	}

	@Override
	protected void save(Connection c) throws SQLException {
		
		PreparedStatement stmt = c.prepareStatement("INSERT INTO con_football_players (id,name,receptions) VALUES (?,?,?)");
		stmt.setLong( 1, this.id );
		stmt.setString( 2, getName() );
		stmt.setLong( 3, getReceptions() );
		
		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	protected void update(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("UPDATE con_football_players set name=?,receptions=? WHERE id=?");
		stmt.setString( 1, getName() );
		stmt.setLong( 2, getReceptions() );
		stmt.setLong( 3, getId() );
		
		stmt.executeUpdate();
		stmt.close();
	}
	
	public String toString() {
		return this.getClass().getCanonicalName() + " name=" + getName() + " receptions=" + getReceptions() + " id=" + id;
	}

	@Override
	protected void delete(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("DELETE FROM con_football_players WHERE id=?");
		stmt.setLong( 1, getId() );
		
		stmt.executeUpdate();
		stmt.close();	
	}

}
