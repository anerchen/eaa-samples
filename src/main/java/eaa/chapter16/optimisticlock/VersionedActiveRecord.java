package eaa.chapter16.optimisticlock;

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
import eaa.support.DB;


public abstract class VersionedActiveRecord {

	private static final long NEW_OBJECT = -1;
	private static final String ID_COLUMN = "ID";
	private static final String VERSION_COLUMN = "version_number";
	private static final String MODIFIED_COLUMN = "modified_by";
	private static final Character Q_MARK = new Character('?');
	
	private long id;
	private long version_number;
	private String modified_by;
	
	private static Map<Class<? extends VersionedActiveRecord>, VersionedClassMapCache> classMappings =
		Collections.synchronizedMap( 
			new HashMap<Class<? extends VersionedActiveRecord>, VersionedClassMapCache>() );
	
	protected VersionedActiveRecord() {
		this.id = NEW_OBJECT;
	}
	
	protected VersionedActiveRecord( long id ) {
		this.id = id;
	}
	
	public final long getId() {
		return id;
	}

	protected final void setId(long id) {
		this.id = id;
	}
	
	public final String getModifiedBy() {
		return modified_by;
	}
	
	protected final void setModifiedby( String modified_by ) {
		this.modified_by = modified_by;
	}
	
	public final long getVersionNumber() {
		return version_number;
	}
	
	protected final void setVersionNumber( long version_number ) {
		this.version_number = version_number;
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
	

	private static <T extends VersionedActiveRecord> VersionedClassMapCache mapClass( Class<T> ar_class ) {
		VersionedClassMapCache rtn = classMappings.get( ar_class );
		if ( rtn == null ) {
			// load all of the fields to map
			rtn = new VersionedClassMapCache( ar_class );
			
			classMappings.put( ar_class, rtn );
		}
		
		return rtn;
	}

	protected static <T extends VersionedActiveRecord> T find( Class<T> ar_class, long id ) {
		VersionedClassMapCache classMapping = mapClass( ar_class );
		
		try {
			// build the select SQL
			String selectSQL = "SELECT " + joinValues( classMapping.getMappedFields().values(), ", " ) + " , " + VERSION_COLUMN + " , " + MODIFIED_COLUMN + " FROM " + classMapping.getTableName() + " WHERE " + ID_COLUMN + "=?";
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
			throw new ApplicationException("Error hydrating " + ar_class.getCanonicalName(), e );
		}
	}
	
	private static <T extends VersionedActiveRecord> void load( T object, Class<T> ar_class, ResultSet results, VersionedClassMapCache classMapping ) throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
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
			Class<? extends VersionedActiveRecord> subClass = resolveClass( ar_class, subClassName );
			
			// see if the sub class has a "Belongsto" field
			VersionedClassMapCache subClassMapping = mapClass( subClass );
			String belongsToField = subClassMapping.getBelongsToField( ar_class.getSimpleName() );
			if ( belongsToField == null ) {
				subClassMapping.getBelongsToField( ar_class.getCanonicalName() );
			}
			Method belongsToSetter = subClassMapping.getSetterMethod( belongsToField );
			
			// load the subordinate collection
			List<? extends VersionedActiveRecord> subList = find( subClass, id_column + "=?", object.id );
			if ( belongsToSetter != null ) {
				for( VersionedActiveRecord listObject : subList ) {
					belongsToSetter.invoke( listObject, object );
				}
			}
			
			Method hasManySetter = VersionedClassMapCache.checkNull( classMapping.getSetterMethod( field ), "On class " + ar_class.getCanonicalName() + " HasMany setter for '" + field + "' could not be found." );
			hasManySetter.invoke( object, subList );
		}		
		
		// load the versioning data
		object.setVersionNumber( results.getLong( VERSION_COLUMN ) );
		object.setModifiedby( results.getString( MODIFIED_COLUMN ) );
	}
	
	private static Class<? extends VersionedActiveRecord> classNameIsActiveRecord( String name ) {
		try {
			Class clazz = VersionedActiveRecord.class.getClassLoader().loadClass(name);
			Object o = clazz.newInstance();
			if ( o instanceof VersionedActiveRecord ) {
				// this class is good 
				return (Class<? extends VersionedActiveRecord>) clazz;
			}
			// check inheritence
		} catch ( Exception ex ) {
		}
		return null;
	}
	
	private static Class<? extends VersionedActiveRecord> resolveClass( Class<? extends VersionedActiveRecord> parent, String subName ) {
		// if no canonical class definition is given try to append the package of the requesting class
		String classNameToTry = subName;
		if ( !classNameToTry.contains(".") && parent.getCanonicalName().contains(".") ) {
			classNameToTry = parent.getCanonicalName().substring(0, parent.getCanonicalName().lastIndexOf(".") + 1 ) + classNameToTry;
		} 
		
		Class<? extends VersionedActiveRecord> clazz = classNameIsActiveRecord( classNameToTry );
		
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
	
	protected static <T extends VersionedActiveRecord> List<T> find( Class<T> ar_class,  String conditions, Object ...params ) {
		int paramCount = countQMarks( conditions );
		if ( paramCount != params.length ) {
			throw new ApplicationException("Parameter count mismatch, conditions '" + conditions + "' specify " + paramCount + " but " + params.length + " provided." );
		}
		
		VersionedClassMapCache classMapping = mapClass( ar_class );
		
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
		VersionedClassMapCache classMapping = mapClass( this.getClass() );
		
		try {
			if ( id == NEW_OBJECT ) {
				this.id = nextId( classMapping.getTableName() );
			}
			
			// fields
			List<String> fields = new ArrayList<String>( classMapping.getMappedFields().values() );
			// add on the reverse side of a belongsTo relationship
			for( String key : classMapping.getBelongsTos().keySet() ) {
				String parentClassName = classMapping.getBelongsTos().get( key ).value();
				Class<? extends VersionedActiveRecord> parentClass = resolveClass( this.getClass(), parentClassName );
				VersionedClassMapCache parentMapping = mapClass( parentClass );
				String identityColumn = parentMapping.getIdentityColumnOfHasManyFromListClass( this.getClass() );
				fields.add( identityColumn );
			}
			
			String insertSQL = "INSERT INTO " + classMapping.getTableName() 
								+ "( id, " + joinValues( fields, ", " ) + " , " + VERSION_COLUMN + " , " + MODIFIED_COLUMN + " )" + 
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
				VersionedActiveRecord parentRecord = (VersionedActiveRecord) getterMethod.invoke( this );
				stmt.setLong( param++, parentRecord.id );
			}
			
			stmt.setObject( param++, 1 );
			stmt.setObject( param++, DB.getUserId() );
			
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
		VersionedClassMapCache classMapping = mapClass( this.getClass() );
		
		try {
			String insertSQL = "UPDATE " + classMapping.getTableName() 
								+ " set " + joinValues( classMapping.getMappedFields().values(), "=?, " ) + "=? " + 
								" , " + VERSION_COLUMN + "=? , " + MODIFIED_COLUMN + "=? " +
								" WHERE id=? and " + VERSION_COLUMN + "=?";
			PreparedStatement stmt = DB.prepare( insertSQL );
			
			int param = 1;
			for( String field : classMapping.getMappedFields().keySet() ) {
				Method method = classMapping.getGetterMethod( field );
				stmt.setObject( param++, method.invoke( this ) );
			}
			stmt.setObject( param++, this.getVersionNumber() + 1 );
			stmt.setObject( param++, DB.getUserId() );
			stmt.setObject( param++, this.getId() );
			stmt.setObject( param++, this.getVersionNumber() );
 			
			int rows = stmt.executeUpdate();
		
			this.setVersionNumber( this.getVersionNumber() + 1 );
			this.setModifiedby( DB.getUserId() );
			
			if ( rows == 0) {
				throw new ApplicationException("Cannot update - concurrency conflict.");
			}
			
		} catch ( Exception ex ) {
			throw new ApplicationException( "Concurrency violation saving object of type " + this.getClass().getCanonicalName(), ex );
		}		
	}
	
	public void destroy() {
		if( id == NEW_OBJECT ) {
			return; // nothing to do
		}
		
		// this will load the mapping for our current class
		VersionedClassMapCache classMapping = mapClass( this.getClass() );
		
		try {
			this.id = nextId( classMapping.getTableName() );
		
			String insertSQL = "DELETE FROM " + classMapping.getTableName() 
								+ " WHERE id=? and " + VERSION_COLUMN + "=? ";
			PreparedStatement stmt = DB.prepare( insertSQL );
			stmt.setObject( 1, this.id );
			stmt.setObject( 2, this.getVersionNumber() );
		
			int rows = stmt.executeUpdate();
			if ( rows == 0) {
				throw new ApplicationException("Cannot delete - concurrency conflict.");
			}
			
		} catch ( Exception ex ) {
			throw new ApplicationException( "There was an error saving an object of class " + this.getClass().getCanonicalName(), ex );
		}
		
	}
	
	
}
