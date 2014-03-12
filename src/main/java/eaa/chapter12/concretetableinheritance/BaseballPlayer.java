package eaa.chapter12.concretetableinheritance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eaa.support.ApplicationException;

public class BaseballPlayer extends Player {

	private long batting_average;

	public BaseballPlayer() {
		super();
	}
	
	public BaseballPlayer( String name, long battingAverage ) {
		super( name );
		this.batting_average = battingAverage;
	}
	
	
	public long getBattingAverage() {
		return batting_average;
	}

	public void setBattingAverage(long batting_average) {
		this.batting_average = batting_average;
	}
	
	@Override
	protected void load( Connection c ) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("SELECT * FROM con_baseball_players WHERE id=?");
		stmt.setLong( 1, getId() );
		ResultSet results = stmt.executeQuery();
		if ( ! results.next() ) {
			throw new ApplicationException("not found - possibly wrong type");
		}
		this.setName( results.getString( "name" ) );
		this.setBattingAverage( results.getLong( "batting_average" ) );
		results.close();
		stmt.close();
	}
	
	@Override
	protected void save(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("INSERT INTO con_baseball_players (id,name,batting_average) VALUES (?,?,?)");
		stmt.setLong( 1, this.id );
		stmt.setString( 2, getName() );
		stmt.setLong( 3, getBattingAverage() );
		
		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	protected void update(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("UPDATE con_baseball_players set name=?,batting_average=? WHERE id=?");
		stmt.setString( 1, getName() );
		stmt.setLong( 2, getBattingAverage() );
		stmt.setLong( 3, getId() );
		
		stmt.executeUpdate();
		stmt.close();
	}
	
	public String toString() {
		return this.getClass().getCanonicalName() + " name=" + getName() + " batting=" + getBattingAverage() + " id=" + id;
	}

	@Override
	protected void delete( Connection c ) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("DELETE FROM con_baseball_players WHERE id=?");
		stmt.setLong( 1, getId() );
		
		stmt.executeUpdate();
		stmt.close();	}

}
