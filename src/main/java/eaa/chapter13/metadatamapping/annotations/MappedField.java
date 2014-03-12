package eaa.chapter13.metadatamapping.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that this field should be mapped to a column in the database 
 * (specified by value)
 * 
 * @author mhelmick
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MappedField {
	String value();
}
