package eaa.chapter12.singletableinheritance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FootballPlayer extends Player {

	private long receptions;
	
	public long getReceptions() {
		return receptions;
	}

	public void setReceptions(long receptions) {
		this.receptions = receptions;
	}

	@Override
	protected void load(ResultSet rs) throws SQLException {
		this.receptions = rs.getLong("receptions");
	}

	@Override
	protected void save(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("INSERT INTO players (id,class,name,receptions) VALUES (?,?,?,?)");
		this.id = KeyGenerator.getNextId(c, "players");
		stmt.setLong( 1, this.id );
		stmt.setString( 2, this.getClass().getCanonicalName() );
		stmt.setString( 3, getName() );
		stmt.setLong( 4, getReceptions() );
		
		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	protected void update(Connection c) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("UPDATE players set name=?,receptions=? WHERE id=?");
		stmt.setLong( 3, getId() );
		stmt.setString( 1, getName() );
		stmt.setLong( 2, getReceptions() );
		
		stmt.executeUpdate();
		stmt.close();
	}
	
	public String toString() {
		return this.getClass().getCanonicalName() + " name=" + getName() + " receptions=" + getReceptions();
	}

}
