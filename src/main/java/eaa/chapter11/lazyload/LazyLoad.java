package eaa.chapter11.lazyload;

public class LazyLoad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		lazyInit();

	}
	
	private static void lazyInit() {
		
		System.out.println("LAZY INITIALIZATION");
		eaa.chapter11.lazyload.lazyinitialization.Person p = eaa.chapter11.lazyload.lazyinitialization.Person.find(1);
		System.out.println("Person " + p.getName() + " has been loaded " );
		
		for( eaa.chapter11.lazyload.lazyinitialization.Bookmark b : p.getBookmarks() ) {
			System.out.println(" bookmark: " + b.getUrl() );
		}
	}

}
