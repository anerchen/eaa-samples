package eaa.chapter12.foreignkeymapping;

public class ForeignKeyMapping {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		testOne();
		testTwo();

	}
	
	private static void testOne() {
		WorkManager manager = new WorkManager();
		
		PersonMapper mapper = new PersonMapper();
		Person person = mapper.find( 1 );
		manager.registerClean( person );
		
		System.out.println( "Person 1 loaded: " + person );
		System.out.println( "Creating new bookmark: www.muohio.edu" );
		Bookmark bm = new Bookmark( "http://www.muohio.edu" );
		bm.setPerson( person );
		manager.registerNew( bm );
		
		manager.commit();
	}
	
	private static void testTwo() {
		WorkManager manager = new WorkManager();
		
		Bookmark bm = new Bookmark( "http://www.muohio.edu" );
		manager.registerNew( bm );
		
		Person p = new Person("Joe Student");
		manager.registerNew( p );
		bm.setPerson( p );
		p.getBookmarks().add( bm );
		
		manager.commit();		
	}

}
