package eaa.chapter12.lob;

public class SerializedLOB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long id = testOne();
		testTwo( id );
		testThree( id );
	}
	
	private static long testOne() {
		LobData d = new LobData();
		d.getDataSet().add("A");
		System.out.println("CREATING LOB OBJECT - CONTAING 1 DATA ITEM");
		return d.insert();
	}
	
	private static void testTwo( long id ) {
		LobData d = LobData.find( id );
		System.out.println("Read back lob object: " + id );
		
		assert( d.getDataSet().contains("A") );
		System.out.println("  contains A" );
		
		for( int i = 0; i < 100; i++ ) {
			d.getDataSet().add( "OBJECT" + i );
			System.out.println("  added '\"OBJECT" + i + "\"" );
		}
		d.update();
		System.out.println("UPDATED");
	}
	
	private static void testThree( long id ) {
		LobData d = LobData.find( id );
		
		System.out.println("Dumping LOB data for ID=" + id );
		for( String s : d.getDataSet() ) {
			System.out.println("  + " + s );
		}
		
		d.update();		
	}

}
