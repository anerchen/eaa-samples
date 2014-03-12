package eaa.chapter10.tabledatagateway;

import java.sql.ResultSet;

public class TableDataGateway {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println("Reading back perosn 1");
		ResultSet results = PersonGateway.findById( 1 );
		results.next();
		printPerson( results );
		
		System.out.println("Inserting John Smith ");
		PersonGateway.insertPerson( "John", "Smith", "230 Kreger Hall","Oxford","OH","45056");
		
		System.out.println("Reading back person 3");
		results = PersonGateway.findById( 3 );
		results.next();
		printPerson( results );
		
		System.out.println("Searching by last name 'Smith'");
		results = PersonGateway.findByLastName( "Smith" );
		while( results.next() ) {
			printPerson( results );
		}
		
		System.out.println("Deleting Person 2");
		PersonGateway.deletePerson( 2 );

	}
	
	public static void printPerson( ResultSet rs ) throws Exception {
		System.out.println(" Person: " +
				rs.getLong(1) + " " +
				rs.getString(2) + " " +
				rs.getString(3) + ", " +
				rs.getString(4) + " " +
				rs.getString(5) + " " +
				rs.getString(6) + ", " +
				rs.getString(7) );
	}

}









