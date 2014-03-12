package eaa.chapter13.queryobject.activemapper;

public class Criteria {

	private String sqlOperator;
	protected String field;
	protected Object value;
	
	private Criteria() { throw new UnsupportedOperationException(); }
	
	private Criteria( String sqlOperator, String field, Object value ) {
		this.sqlOperator = sqlOperator;
		this.field = field;
		this.value = value;
	}
	
	public static Criteria greaterThan( String fieldName, Object value ) {
		return new Criteria( " > ", fieldName, value );
	}
	
	public static Criteria lessThan( String fieldName, Object value ) {
		return new Criteria( " < ", fieldName, value );
	}
	
	public static Criteria equalTo( String fieldName, Object value ) {
		return new Criteria( " = ", fieldName, value );
	}
	
	public static Criteria notEqualTo( String fieldName, Object value ) {
		return new Criteria( " != ", fieldName, value );
	}

	public String getField() {
		return field;
	}

	public String getSqlOperator() {
		return sqlOperator;
	}

	public Object getValue() {
		return value;
	}
	
}
