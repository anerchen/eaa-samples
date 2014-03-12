package eaa.chapter12.associationtablemapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import eaa.support.ApplicationException;

public class EmployeeMapper extends AbstractMapper<Employee> {

	public static final String COLUMNS = " id, first_name, last_name ";
	public static final String TABLE = "employees";
	
	private static final String UPDATE_STATEMENT = "UPDATE " + TABLE + " SET " +
			" first_name=?, last_name=? WHERE id = ? ";
	
	protected String table() {
		return TABLE;
	}
	
	protected String findStatement() {
		// THIS DOES *** NOT *** WORK IF THE EMPLOYEE HAS NO SKILLS
		return "SELECT e.id,e.first_name,e.last_name,s.id,s.name " +
		 	   "FROM employees e, skills s, employees_skills es " +
		       "WHERE es.employee_id = e.id " +
		       "and es.skill_id = s.id " +
		       "and e.id = ? ";
	}
	
	protected String insertStatement() {
		return "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?,?,?)";
	}
	
	public Employee find( long id ) {
		return abstractFind(id);
	}
	
	public List<Employee> find() {
		Connection c = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			c = openConnection();
			
			stmt = c.prepareStatement( "SELECT " + COLUMNS + " FROM " + TABLE );
			rs = stmt.executeQuery();
			
			return loadAll( rs );
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			releaseConnection( c, stmt, rs );
		}
	}
	
	public void update( Connection c, Employee person ) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.prepareStatement( UPDATE_STATEMENT );
			
			stmt.setString( 1, person.getFirstName() );
			stmt.setString( 2, person.getLastName() );
			stmt.setLong(   2, person.getId() );
			stmt.executeUpdate();
			
			preDelete( c, person );
			postInsert( c, person );
			
		} catch ( SQLException ex ) {
			throw new ApplicationException(ex);
		} finally {
			release( stmt, rs );
		}
	}
	
	@Override
	protected void postInsert( Connection c, Employee person ) throws SQLException {
		PreparedStatement stmt = null;
		for ( Skill skill : person.getSkills() ) {
			stmt = c.prepareStatement("INSERT INTO employees_skills (employee_id,skill_id) VALUES(?,?)");
			stmt.setLong( 1, person.getId() );
			stmt.setLong( 2, skill.getId() );
			stmt.executeUpdate();
		}
	}

	@Override
	protected void preDelete( Connection c, Employee person ) throws SQLException {
		PreparedStatement stmt = c.prepareStatement("DELETE FROM employees_skills WHERE employee_id=?");
		stmt.setLong( 1, person.getId() );
		stmt.executeUpdate();
	}

	@Override
	protected Employee doLoad(long id, ResultSet rs) throws SQLException {
		String first_name = rs.getString( 2 );
		String last_name = rs.getString( 3 );
		Employee e = new Employee( id, first_name, last_name );
		long skill_id = rs.getLong( 4 );
		String skill_name  = rs.getString( 5 );
		e.getSkills().add( new Skill( skill_id, skill_name ) );
		
		while( rs.next() ) {
			skill_id = rs.getLong( 4 );
			skill_name  = rs.getString( 5 );
			e.getSkills().add( new Skill( skill_id, skill_name ) );
		}
		
		return e;
	}
	
	protected void prepareInsert( PreparedStatement stmt, Employee person ) throws SQLException {
		stmt.setLong( 1, person.getId() );
		stmt.setString( 2, person.getFirstName() );
		stmt.setString( 2, person.getLastName() );
	}
	
}
