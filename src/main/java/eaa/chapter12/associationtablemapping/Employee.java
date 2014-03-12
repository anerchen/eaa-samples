package eaa.chapter12.associationtablemapping;

import java.util.ArrayList;
import java.util.List;

public class Employee extends DomainObject {

	private String first_name;
	private String last_name;
	private List<Skill> skills = new ArrayList<Skill>();
	
	private static EmployeeMapper manager = new EmployeeMapper();
	
	public Employee( long id, String first_name, String last_name ) {
		super(id);
		this.first_name = first_name;
		this.last_name = last_name;
	}

	@Override
	public boolean equals(DomainObject o) {
		return this.getId() == o.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractMapper getMapper() {
		return manager;
	}
	
	

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	@Override
	public String toString() {
		String s = "name='" + getFirstName() + " " + getLastName() + "' id=" + getId();
		for( Skill skill : skills ) {
			s += "\n" + skill.toString();
		}
		return s;
	}
	
}
