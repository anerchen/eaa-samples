package eaa.chapter13.queryobject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicate that this class should be mapped to a table in the database
 * 
 * The value is required and should be the name of the database talbe to map to
 * 
 * @author mhelmick
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedTable {
	String value();
}
