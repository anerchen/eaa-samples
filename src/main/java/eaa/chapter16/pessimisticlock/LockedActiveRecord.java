package eaa.chapter16.pessimisticlock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eaa.support.ApplicationException;
import eaa.support.ConcurrencyException;


public abstract class LockedActiveRecord {

	private static final long NEW_OBJECT = -1;
	private static final String ID_COLUMN = "ID";
	private static final Character Q_MARK = new Character('?');
	
	private long id;
	
	private static Map<Class<? extends LockedActiveRecord>, ClassMapCache> classMappings =
		Collections.synchronizedMap( 
			new HashMap<Class<? extends LockedActiveRecord>, ClassMapCache>() );
	
	protected LockedActiveRecord() {
		this.id = NEW_OBJECT;
	}
	
	protected LockedActiveRecord( long id ) {
		this.id = id;
	}
	
	public final long getId() {
		return id;
	}

	protected final void setId(long id) {
		this.id = id;
	}
		
	private static String joinValues( Collection<String> values , String joiner ) {
		StringBuffer buf = new StringBuffer();
		
		int count = 0;
		for( String value : values ) {
			buf.append( value );
			count++;
			if ( count < values.size() ) {
				buf.append(joiner);
			}
		}
		
		return buf.toString();
	}
	

	private static <T extends LockedActiveRecord> ClassMapCache mapClass( Class<T> ar_class ) {
		ClassMapCache rtn = classMappings.get( ar_class );
		if ( rtn == null ) {
			// load all of the fields to map
			rtn = new ClassMapCache( ar_class );
			
			classMappings.put( ar_class, rtn );
		}
		
		return rtn;
	}

	protected static <T extends LockedActiveRecord> T find( Class<T> ar_class, long id ) throws ConcurrencyException {
		ClassMapCache classMapping = mapClass( ar_class );
		
		try {
			// get lock
			ExclusiveReadLockManager.acquireLock( classMapping.getTableName(), id, DB.getUserId() );
			
			// build the select SQL
			String selectSQL = "SELECT " + joinValues( classMapping.getMappedFields().values(), ", " ) + " FROM " + classMapping.getTableName() + " WHERE " + ID_COLUMN + "=?";
			PreparedStatement stmt = DB.prepare( selectSQL );
			stmt.setLong( 1, id );
			
			ResultSet results = stmt.executeQuery();
			if ( results.next() ) {
				// create the instance and populate the parameters
				T rtn = ar_class.newInstance();
				rtn.id = id;
			
				load( rtn, ar_class, results, classMapping );
				
				return rtn;
			}
			throw new ApplicationException("No record found on table '" + classMapping.getTableName() + "' for id=" + id );
		} catch (Exception e) {
			ExclusiveReadLockManager.releaseLock( classMapping.getTableName(), id, DB.getUserId() );
			throw new ApplicationException("Error hydrating " + ar_class.getCanonicalName(), e );
		}
	}
	
	private static <T extends LockedActiveRecord> void load( T object, Class<T> ar_class, ResultSet results, ClassMapCache classMapping ) throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// go over the fields
		for( String field : classMapping.getMappedFields().keySet() ) {
			String column = classMapping.getMappedFields().get( field );
			
			Object dbValue = results.getObject( column );
			
			Method method = classMapping.getSetterMethod( field );
			method.invoke( object, dbValue );
			
		}
		
		// go over the collections - the "HasMany" relationships
		for( String field : classMapping.getHasManys().keySet() ) {
			String subClassName = classMapping.getHasManys().get( field ).listClass();
			String id_column = classMapping.getHasManys().get( field ).identityColumn();
			Class<? extends LockedActiveRecord> subClass = resolveClass( ar_class, subClassName );
			
			// see if the sub class has a "Belongsto" field
			ClassMapCache subClassMapping = mapClass( subClass );
			String belongsToField = subClassMapping.getBelongsToField( ar_class.getSimpleName() );
			if ( belongsToField == null ) {
				subClassMapping.getBelongsToField( ar_class.getCanonicalName() );
			}
			Method belongsToSetter = subClassMapping.getSetterMethod( belongsToField );
			
			// load the subordinate collection
			List<? extends LockedActiveRecord> subList = find( subClass, id_column + "=?", object.id );
			if ( belongsToSetter != null ) {
				for( LockedActiveRecord listObject : subList ) {
					belongsToSetter.invoke( listObject, object );
				}
			}
			
			Method hasManySetter = ClassMapCache.checkNull( classMapping.getSetterMethod( field ), "On class " + ar_class.getCanonicalName() + " HasMany setter for '" + field + "' could not be found." );
			hasManySetter.invoke( object, subList );
		}		
		
	}
	
	private static Class<? extends LockedActiveRecord> classNameIsActiveRecord( String name ) {
		try {
			Class clazz = LockedActiveRecord.class.getClassLoader().loadClass(name);
			Object o = clazz.newInstance();
			if ( o instanceof LockedActiveRecord ) {
				// this class is good 
				return (Class<? extends LockedActiveRecord>) clazz;
			}
			// check inheritence
		} catch ( Exception ex ) {
		}
		return null;
	}
	
	private static Class<? extends LockedActiveRecord> resolveClass( Class<? extends LockedActiveRecord> parent, String subName ) {
		// if no canonical class definition is given try to append the package of the requesting class
		String classNameToTry = subName;
		if ( !classNameToTry.contains(".") && parent.getCanonicalName().contains(".") ) {
			classNameToTry = parent.getCanonicalName().substring(0, parent.getCanonicalName().lastIndexOf(".") + 1 ) + classNameToTry;
		} 
		
		Class<? extends LockedActiveRecord> clazz = classNameIsActiveRecord( classNameToTry );
		
		if ( clazz == null ) {
			// try just the name
			clazz = classNameIsActiveRecord( subName );
		}
		
		if ( clazz == null ) {
			// if we still can't find it - throw
			throw new ApplicationException("Could not resolve a class named '" + subName + "' in the context of '" + parent.getCanonicalName() + "'." );
		}
		
		return clazz;
	}
	
	private static int countQMarks( String x ) {
		int count = 0;
		for( Character c : x.toCharArray() ) {
			if ( c.equals( Q_MARK ) )  {
				count++;
			}
		}
		return count;
	}
	
	protected static <T extends LockedActiveRecord> List<T> find( Class<T> ar_class,  String conditions, Object ...params ) {
		int paramCount = countQMarks( conditions );
		if ( paramCount != params.length ) {
			throw new ApplicationException("Parameter count mismatch, conditions '" + conditions + "' specify " + paramCount + " but " + params.length + " provided." );
		}
		
		ClassMapCache classMapping = mapClass( ar_class );
		
		String selectSQL = "SELECT * FROM " + classMapping.getTableName() + " WHERE " + conditions;
		List<T> list = new ArrayList<T>();
		try {
			PreparedStatement stmt = DB.prepare( selectSQL );
			paramCount = 1;
			for( Object param : params ) {
				stmt.setObject( paramCount++, param );
			}
			
			ResultSet rs = stmt.executeQuery();
			
			while( rs.next() ) {
				T object = ar_class.newInstance();
				object.id = rs.getLong( ID_COLUMN );
				
				ExclusiveReadLockManager.acquireLock( classMapping.getTableName(), object.id, DB.getUserId() );
				
				load( object, ar_class, rs, classMapping );
				
				list.add( object );
			}
			
			rs.close();
			stmt.close();
		} catch ( Exception ex ) {
			throw new ApplicationException( "Error loading collection.", ex );
		}
			
		return list;
	}
	
	private String qMarks( int number ) {
		StringBuffer b = new StringBuffer();
		for( int i = 0; i < number; i++ ) {
			b = b.append("?,");
		}
		return b.toString().substring(0,b.length()-1);
	}
	
	private long nextId( String table ) throws SQLException {
		PreparedStatement stmt = DB.prepare("SELECT next_id FROM app_keys WHERE table_name=?");
		stmt.setString( 1, table );
		ResultSet results = stmt.executeQuery();
		results.next();
		long nextValue = results.getLong(1);
		
		
		// OPTIMISTIC OFFLINE LOCK
		stmt = DB.prepare("UPDATE app_keys SET next_id=? WHERE table_name=? and next_id=?");
		stmt.setLong( 1, nextValue + 1 );
		stmt.setString( 2, table );
		stmt.setLong( 3, nextValue );
		stmt.executeUpdate();
		
		return nextValue;
	}
	
	public long insert() {
		// this will load the mapping for our current class
		ClassMapCache classMapping = mapClass( this.getClass() );
		
		try {
			if ( id == NEW_OBJECT ) {
				this.id = nextId( classMapping.getTableName() );
			}
			
			// fields
			List<String> fields = new ArrayList<String>( classMapping.getMappedFields().values() );
			// add on the reverse side of a belongsTo relationship
			for( String key : classMapping.getBelongsTos().keySet() ) {
				String parentClassName = classMapping.getBelongsTos().get( key ).value();
				Class<? extends LockedActiveRecord> parentClass = resolveClass( this.getClass(), parentClassName );
				ClassMapCache parentMapping = mapClass( parentClass );
				String identityColumn = parentMapping.getIdentityColumnOfHasManyFromListClass( this.getClass() );
				fields.add( identityColumn );
			}
			
			String insertSQL = "INSERT INTO " + classMapping.getTableName() 
								+ "( id, " + joinValues( fields, ", " ) + " )" + 
								" VALUES (" + qMarks( 1 + fields.size() + 2 )+ ")";
			PreparedStatement stmt = DB.prepare( insertSQL );
			stmt.setObject( 1, this.id );
			
			int param = 2;
			for( String field : classMapping.getMappedFields().keySet() ) {
				Method method = classMapping.getGetterMethod( field );
				stmt.setObject( param++, method.invoke( this ) );
			}
			// set the BelongsTo identity column appropriately
			for( String key : classMapping.getBelongsTos().keySet() ) {
				Method getterMethod = classMapping.getGetterMethod( key );
				LockedActiveRecord parentRecord = (LockedActiveRecord) getterMethod.invoke( this );
				stmt.setLong( param++, parentRecord.id );
			}
			
			
			stmt.executeUpdate();
		
		} catch ( Exception ex ) {
			throw new ApplicationException( "There was an error saving an object of class " + this.getClass().getCanonicalName(), ex );
		}
		
		
		return getId();
	}
	
	public void update() {
		if( id == NEW_OBJECT ) {
			throw new ApplicationException("Cannot update a new row.");
		}
		
		// this will load the mapping for our current class
		ClassMapCache classMapping = mapClass( this.getClass() );
		
		try {
			ExclusiveReadLockManager.verifyLock( classMapping.getTableName(), this.getId(), DB.getUserId() );
			
			String insertSQL = "UPDATE " + classMapping.getTableName() 
								+ " set " + joinValues( classMapping.getMappedFields().values(), "=?, " ) + "=? " + 
								" WHERE id=? ";
			PreparedStatement stmt = DB.prepare( insertSQL );
			
			int param = 1;
			for( String field : classMapping.getMappedFields().keySet() ) {
				Method method = classMapping.getGetterMethod( field );
				stmt.setObject( param++, method.invoke( this ) );
			}
			stmt.setObject( param++, this.getId() );
			
			stmt.executeUpdate();
			
		} catch ( Exception ex ) {
			throw new ApplicationException( "There was an error saving an object of class " + this.getClass().getCanonicalName(), ex );
		}		
	}
	
	public void destroy() {
		if( id == NEW_OBJECT ) {
			return; // nothing to do
		}
		
		// this will load the mapping for our current class
		ClassMapCache classMapping = mapClass( this.getClass() );
		
		try {
			ExclusiveReadLockManager.verifyLock( classMapping.getTableName(), this.getId(), DB.getUserId() );
			
			this.id = nextId( classMapping.getTableName() );
		
			String insertSQL = "DELETE FROM " + classMapping.getTableName() 
								+ " WHERE id=? ";
			PreparedStatement stmt = DB.prepare( insertSQL );
			stmt.setObject( 1, this.id );
			
			stmt.executeUpdate();
			
		} catch ( Exception ex ) {
			throw new ApplicationException( "There was an error saving an object of class " + this.getClass().getCanonicalName(), ex );
		}
		
	}
	
	
}
