package eaa.chapter13.queryobject.activemapper;

import java.util.ArrayList;
import java.util.List;

public class Query<E extends ActiveRecord> {

	private Class<E> clazz;
	private List<Criteria> criteria = new ArrayList<Criteria>();
	
	private Query() { 
		throw new UnsupportedOperationException(); }
	
	public Query( Class<E> clazz ) {
		this.clazz = clazz;
	}
	
	public void add( Criteria c ) {
		criteria.add( c );
	}
	
	public Class<? extends ActiveRecord> getQueryClass() {
		return clazz;
	}
	
	public List<Criteria> criteria() {
		return criteria;
	}
	
}
