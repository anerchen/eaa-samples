package eaa.chapter13.metadatamapping;

import java.util.List;

import eaa.support.DB;

public class MetadataMapping {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		testOne();
		testTwo();
		testThree();
		testFour();
		testFive();
	}
	
	public static void testOne() {
		Person p = Person.find(1);
		System.out.println( p );
		DB.rollback();
	}
	
	public static void testTwo() throws Exception {
		Person p = new Person();
		p.setName( "Mike" );
		p.setCity("Cincinnati");
		p.setState("Ohio");
		p.insert();
		DB.commit();
		System.out.println(p);
	}
	
	public static void testThree() throws Exception {
		Person p = Person.find(2);
		p.setCity("Batesville");
		p.setState("Indiana");
		p.update();
		DB.commit();
		System.out.println(p);
	}
	
	public static void testFour() throws Exception {
		Person p = new Person();
		p.setName( "Miami Student" );
		p.setCity( "Oxford" );
		p.setState( "Ohio" );
		p.insert();
		
		Bookmark bm = new Bookmark( p, "http://www.muohio.edu" );
		p.getBookmarks().add( bm );
		bm.insert();
		
		DB.commit();
		
		System.out.println( p );
	}
	
	public static void testFive() throws Exception {
		List<Person> people = Person.find("state=?", "Ohio" );
		for( Person p : people ) {
			System.out.println(p);
		}
		DB.rollback();
	}

}
