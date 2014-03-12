package eaa.chapter10.activerecord;

import java.util.List;


public class ActiveRecord {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Reading back perosn 1");
		Person p = Person.find( 1 );
		printPerson( p );
		
		System.out.println("Inserting John Smith ");
		p = new Person( "John", "Smith", "230 Kreger Hall","Oxford","OH","45056" );
		p.save();
		
		System.out.println("Reading back person 3");
		p = Person.find( 3 );
		printPerson( p );
		
		System.out.println("Reading back all people");
		List<Person> list = Person.find();
		for( Person p1 : list ) {
			printPerson( p1 );
		}
		
		System.out.println("Deleting Person 2");
		p = Person.find( 2 );
		p.destroy();
		
		System.out.println("Update person 3");
		p = Person.find( 3 );
		p.setCity( "Cincinnati" );
		p.setZipCode( "45208" );
		p.save();
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
