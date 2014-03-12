package eaa.chapter15.client;

import java.util.ArrayList;
import java.util.List;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/** */
		PersonServiceService serviceLocator = new PersonServiceService();
		PersonService service = serviceLocator.getPersonServicePort();
		
		testOne( service );
		
	}
	
	public static void testOne( PersonService service ) {
	
		try {
			List<String> bookmarks = new ArrayList<String>();
			bookmarks.add( "http://www.muohio.edu/" );
			bookmarks.add( "http://www.google.com" );
			long id = service.createPerson( "Web Services Person",  bookmarks );
			System.out.println("Created person id=" + id ); 
			
			PersonDTO person = service.findPerson( id );
			System.out.println("name = " + person.getName() );
			for( String bookmark : person.getBookmarks() ) {
				System.out.println(" bookmark => " + bookmark );
			}
			
		} catch (ApplicationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
// **/		
	}

}
