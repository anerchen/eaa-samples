package eaa.chapter16.optimisticlock;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import activemapper.BelongsTo;
import activemapper.HasMany;
import activemapper.MappedField;
import activemapper.MappedTable;
import eaa.support.ApplicationException;

class VersionedClassMapCache {

	private Class<? extends VersionedActiveRecord> baseClass;
	private Map<String,Field> fieldObjects = new HashMap<String,Field>();
	
	private Map<String,String> mappedFields = new LinkedHashMap<String,String>();
	private Map<String,HasMany> hasManys = new LinkedHashMap<String,HasMany>();
	private Map<String,BelongsTo> belongsTos = new LinkedHashMap<String,BelongsTo>();
	
	private Map<String,Method> getterMethods = new HashMap<String,Method>();
	private Map<String,Method> setterMethods = new HashMap<String,Method>();
	
	private String tableName;
	
	public VersionedClassMapCache( Class<? extends VersionedActiveRecord> baseClass ) {
		this.baseClass = baseClass;
		
		// get the table name
		MappedTable tableNameAttr = baseClass.getAnnotation( MappedTable.class );
		VersionedClassMapCache.checkNull( tableNameAttr, "Class " + baseClass.getCanonicalName() + " does not have the " + MappedTable.class.getCanonicalName() + " attribute present.");
		tableName = tableNameAttr.value();
		VersionedClassMapCache.checkNull( tableName, "Class " + baseClass.getCanonicalName() + " is a mapped table, but has not associated table." );
		
		
		Field[] declaredFields = this.baseClass.getDeclaredFields();
		Method[] methods = this.baseClass.getDeclaredMethods();
		for( Field field : declaredFields ) {
			fieldObjects.put( field.getName(), field );
			
			MappedField mappedField = field.getAnnotation( MappedField.class );
			if ( mappedField != null ) {
				String fieldName = field.getName();
				String columnName = mappedField.value();
				checkNull( columnName, "On class " + this.baseClass.getCanonicalName() + ", field " + fieldName + " is a MappedField, but no DB column is specified.");
				mappedFields.put( fieldName, columnName );
			}
			
			HasMany hasMany = field.getAnnotation( HasMany.class );
			if ( hasMany != null ) {
				String fieldName = field.getName();
				String columnName = hasMany.listClass();
				checkNull( columnName, "On class " + this.baseClass.getCanonicalName() + ", collection " + fieldName + " is in a HasMany relationship, but no subbordinate table is specified.");
				hasManys.put( fieldName, hasMany );
			}
			
			BelongsTo belongsTo = field.getAnnotation( BelongsTo.class );
			if ( belongsTo != null ) {
				String fieldName = field.getName();
				String className = belongsTo.value();
				checkNull( className, "On class " + this.baseClass.getCanonicalName() + ", field " + fieldName + " is a BelongsTo relationship, but not owning class was specified." );
				belongsTos.put( fieldName, belongsTo );
			} 
			
			if ( mappedField != null || hasMany != null || belongsTo != null ) {
				// get setter and getter
				String setterName = setterMethod( field.getName() );
				String getterName = getterMethod( field.getName() );
				
				for( Method method : methods ) {
					if ( method.getName().equals( setterName ) ) {
						setterMethods.put( field.getName(), method );
					} else if ( method.getName().equals( getterName ) ) {
						getterMethods.put( field.getName(), method );
					}
				} 
			}
		}
		
	}
	
	public Method getSetterMethod( String field ) {
		return checkNull( setterMethods.get(field), "On class " + this.baseClass.getCanonicalName() + ", requested field '" + field + "' has no, or an incorrectley named, setter method." );
	}
	
	public Method getGetterMethod( String field ) {
		return checkNull( getterMethods.get(field), "On class " + this.baseClass.getCanonicalName() + ", requested field '" + field + "' has no, or an incorrectley named, getter method." );
	}
	
	private static String setterMethod( String field ) {
		return "set" + field.substring(0,1).toUpperCase() + field.substring(1);
	}
	
	private static String getterMethod( String field ) {
		return "get" + field.substring(0,1).toUpperCase() + field.substring(1);
	}
	
	public String getBelongsToField( String parentClass ) {
		for( String key : belongsTos.keySet() ) {
			if ( belongsTos.get(key).value().equals(parentClass) ) {
				return key;
			}
		}
		return null;
	}
	
	public String getIdentityColumnOfHasManyFromListClass( Class<? extends VersionedActiveRecord> child ) {
		for( HasMany hasMany : hasManys.values() ) {
			if ( hasMany.listClass().equals( child.getSimpleName() ) || hasMany.listClass().equals( child.getCanonicalName() ) ) {
				return hasMany.identityColumn();
			}
		}
		return null;
	}
	
	public Map<String,BelongsTo> getBelongsTos() {
		return belongsTos;
	}
 	
	public Field getDeclaredField( String field ) {
		return fieldObjects.get( field );
	}
	
	static <T> T checkNull( T o , String msg ) {
		if ( o == null ) {
			throw new ApplicationException( msg );
		}
		return o;
	}

	public Class<? extends VersionedActiveRecord> getBaseClass() {
		return baseClass;
	}

	public Map<String, HasMany> getHasManys() {
		return hasManys;
	}

	public Map<String, String> getMappedFields() {
		return mappedFields;
	}

	public String getTableName() {
		return tableName;
	}

	
}
