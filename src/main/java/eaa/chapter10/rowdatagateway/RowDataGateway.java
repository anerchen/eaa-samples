package eaa.chapter10.rowdatagateway;

import java.util.List;

public class RowDataGateway {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println("Reading back perosn 1");
		Person p = PersonFinder.findById( 1 );
		printPerson( p );
		
		System.out.println("Inserting John Smith ");
		p = new PersonGateway( "John", "Smith", "230 Kreger Hall","Oxford","OH","45056" );
		p.insert();
		
		System.out.println("Reading back person 3");
		p = PersonFinder.findById( 3 );
		printPerson( p );
		
		System.out.println("Reading back all people");
		List<Person> list = PersonFinder.findAll();
		for( Person p1 : list ) {
			printPerson( p1 );
		}
		
		System.out.println("Deleting Person 2");
		p = PersonFinder.findById( 2 );
		p.delete();

	}
	
	public static void printPerson( Person p ) throws Exception {
		System.out.println(" Person: " +
				p.getId() + " " +
				p.getFirstName() + " " +
				p.getLastName() + ", " +
				p.getAddress() + " " +
				p.getCity() + " " +
				p.getState() + ", " +
				p.getZipCode() );
	}

}
