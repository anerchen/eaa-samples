package eaa.chapter12.singletableinheritance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseballPlayer extends Player {

	private long batting_average;
	
	
	
	public long getBattingAverage() {
		return batting_average;
	}

	public void setBattingAverage(long batting_average) {
		this.batting_average = batting_average;
	}

	@Override
	protected void load(ResultSet rs) throws SQLException {
		this.batting_average = rs.getLong("batting_average");
	}
	
	@Override
	protected void save(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("INSERT INTO players (id,class,name,batting_average) VALUES (?,?,?,?)");
		this.id = KeyGenerator.getNextId(c, "players");
		stmt.setLong( 1, this.id );
		stmt.setString( 2, this.getClass().getCanonicalName() );
		stmt.setString( 3, getName() );
		stmt.setLong( 4, getBattingAverage() );
		
		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	protected void update(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("UPDATE players set name=?,batting_average=? WHERE id=?");
		stmt.setLong( 3, getId() );
		stmt.setString( 1, getName() );
		stmt.setLong( 2, getBattingAverage() );
		
		stmt.executeUpdate();
		stmt.close();
	}
	
	public String toString() {
		return this.getClass().getCanonicalName() + " name=" + getName() + " batting=" + getBattingAverage();
	}

}
