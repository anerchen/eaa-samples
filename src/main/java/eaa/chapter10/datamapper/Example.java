package eaa.chapter10.datamapper;

import java.util.ArrayList;
import java.util.List;


public class Example {

	public static void test() {
		
		List<Person> list = new ArrayList<Person>();
		
		Person p = searchable( list, 1 );

		
	}
	
	
	public static <T extends DomainObject> T searchable( List<T> list, long id ) {
		for( T dObj : list ) {
			if ( dObj.getId() == id ) {
				return dObj;
			}
		}
		return null;
	}
	
}
