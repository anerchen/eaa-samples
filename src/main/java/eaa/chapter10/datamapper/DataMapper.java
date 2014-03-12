package eaa.chapter10.datamapper;

import java.util.List;

public class DataMapper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PersonMapper mapper = new PersonMapper();
		
		System.out.println("Reading back perosn 1");
		Person p = mapper.find( 1 );
		printPerson( p );
		
		System.out.println("Inserting John Smith ");
		p = new Person( "John", "Smith", "230 Kreger Hall","Oxford","OH","45056" );
		mapper.insert( p );
		
		System.out.println("Reading back person 3");
		p = mapper.find( 3 );
		printPerson( p );
		
		System.out.println("Reading back all people");
		List<Person> list = mapper.find();
		for( Person p1 : list ) {
			printPerson( p1 );
		}
		
		System.out.println("Deleting Person 2");
		p = mapper.find( 2 );
		mapper.destroy( p );
		
		System.out.println("Update person 3");
		p = mapper.find( 3 );
		p.setCity( "Cincinnati" );
		p.setZipCode( "45208" );
		mapper.update( p );
		printPerson( p );

	}
	
	public static void printPerson( Person p ) {
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
