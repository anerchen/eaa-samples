package eaa.support;

public class ConcurrencyException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4803438051455972520L;

	public ConcurrencyException() {
		super();
	}
	
	public ConcurrencyException( String arg ) {
		super( arg );
	}
	
	public ConcurrencyException( String arg, Throwable arg1 ) {
		super( arg, arg1 );
	}
	
	public ConcurrencyException( Throwable arg ) {
		super( arg );
	}
	
}
