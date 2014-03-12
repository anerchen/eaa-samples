package eaa.chapter12.concretetableinheritance;

public class ClassTableInheritance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Creating football player");
		Player fb = new FootballPlayer( "Football Player", 12 );
		long fbid = fb.insert();
		System.out.println("  inserted -> " + fb );
		
		System.out.println("Creating baseball player");
		Player bb = new BaseballPlayer( "Baseball Player", 123 );
		long bbid = bb.insert();
		System.out.println("  inserted -> " + bb );

		fb = Player.find( fbid );
		System.out.println("Loaded -> " + fb );
		((FootballPlayer)fb).setReceptions( 23 );
		fb.update();
		System.out.println("Updated -> " + fb );
		
		bb = Player.find( bbid );
		System.out.println("Loaded -> " + bb );
		bb.destroy();
		System.out.println("Deleted");
		
	}

}
