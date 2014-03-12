package eaa.support;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1535083631731225676L;

	public ApplicationException() {
		super();
	}
	
	public ApplicationException( String arg ) {
		super( arg );
	}
	
	public ApplicationException( String arg, Throwable arg1 ) {
		super( arg, arg1 );
	}
	
	public ApplicationException( Throwable arg ) {
		super( arg );
	}
	
}
