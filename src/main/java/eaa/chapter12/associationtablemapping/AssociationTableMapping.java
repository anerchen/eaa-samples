package eaa.chapter12.associationtablemapping;

public class AssociationTableMapping {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EmployeeMapper m = new EmployeeMapper();
		
		Employee e = m.find( 1 );
		
		System.out.println( e );

	}

}
