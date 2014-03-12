package eaa.chapter13.queryobject;

import eaa.chapter13.queryobject.activemapper.Criteria;
import eaa.chapter13.queryobject.activemapper.Query;
import eaa.support.DB;

public class QueryObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		testOne();
		testTwo();
		testThree();
	}
	
	public static void testOne() {
		Query<Person> query = new Query<Person>( Person.class );
		query.add( Criteria.equalTo( "name", "Test Person" ) );
		
		Person p = Person.find( query ).iterator().next();
		
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
		Query<Person> query = new Query<Person>( Person.class );
		query.add( Criteria.equalTo( "city", "Cincinnati" ) );
		query.add( Criteria.equalTo( "state", "Ohio" ) );
		
		Person p = Person.find( query ).iterator().next();
		
		p.setCity("Batesville");
		p.setState("Indiana");
		p.update();
		DB.commit();
		System.out.println(p);
	}
	

}
