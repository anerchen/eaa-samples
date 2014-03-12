package eaa.chapter12.identityfield;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IdentityField {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<Runnable> runners = new ArrayList<Runnable>();
		runners.add( new Tester("people", "test01" ) );
		runners.add( new Tester("people", "test02" ) );
		runners.add( new Tester("bookmarks", "test03" ) );
		runners.add( new Tester("bookmarks", "test04" ) );
		
		// start
		ThreadGroup g = new ThreadGroup("runners");
		List<Thread> threads = new ArrayList<Thread>();
		for( Runnable r : runners ) {
			Thread t = (new Thread( g, r ));
			threads.add(t);
			t.start();
		}
		
		while( ! threads.isEmpty() ) {
			Iterator<Thread> iter = threads.iterator();
			while( iter.hasNext() ) {
				Thread t = iter.next();
				if ( ! t.isAlive() ) {
					threads.remove( t );
					iter = threads.iterator();
				}
			}
			try { Thread.sleep(100); } catch ( InterruptedException iex ) {}
		}
		
		
	}

	
	private static class Tester implements Runnable {
		
		private String table;
		private String me;
		public Tester( String table, String me ) {
			this.table = table;
			this.me = me;
		}
	
		public void run() {
			
			for( int i = 1; i < 100; i++ ) {
				long id = KeyGenerator.getNextId( table );
				System.out.println( me + ": table=" + table + "  got Id=" + id );
			}
			
		}
		
	}
	
}
