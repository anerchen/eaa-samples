package eaa.chapter11.identitymap;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		testOne();
		testTwo();
		testThree();
		
	}
	
	public static void testOne() {
		WorkManager m = new WorkManager();
		
		FavoriteMapper mapper = new FavoriteMapper();
		Favorite f = new Favorite("http://www.yahoo.com",1);
		m.registerNew( f );
		
		Favorite f2 = mapper.find( 1 );
		m.registerClean( f2 );
		
		f2.setVisits( f2.getVisits() + 1 );
		m.registerDirty( f2 );
		
		m.commit();
	}
	
	public static void testTwo() {

		WorkManager m = new WorkManager();
		
		FavoriteMapper mapper = new FavoriteMapper();
		Favorite f1 = mapper.find( 1 );
		m.registerClean( f1 );
		Favorite f2 = mapper.find( 2 );
		m.registerClean( f2 );
		
		Favorite f3 = new Favorite("http://www.ask.com",1);
		m.registerNew( f3 );
		
		f1.setVisits( f1.getVisits() + 1 );
		m.registerDirty( f1 );
		f2.setVisits( f2.getVisits() + 1 );
		m.registerDirty( f2 );
		
		Favorite f4 = new Favorite("http://www.shouldn't see me.com",1);
		m.registerNew( f4 );
		m.registerDeleted( f4 );
		
		m.registerDeleted( f2 );
		
		m.commit();		
	}
	
	public static void testThree() {

		WorkManager m = new WorkManager();
		
		FavoriteMapper mapper = new FavoriteMapper();
		Favorite f1 = mapper.find( 1 );
		m.registerClean( f1 );
		
		f1 = mapper.find(1);
		
		for( int i = 0; i < 10; i++ ) {
			Favorite f = new Favorite("http://" + i + ".com", 1);
			m.registerNew(f);
		}
		
		m.commit();	
		
		m = new WorkManager();
		
		mapper = new FavoriteMapper();
		
		Favorite f = mapper.find( 1 );
		for( int i = 3; i <= 10+3; i++ ) {
			f = mapper.find( i );
			m.registerClean( f );
		}
		for( int i = 3; i <= 10+3; i++ ) {
			f = mapper.find( i );
			m.registerClean( f );
		}
		
		m.commit();	
		
	}


}
