package eaa.chapter13.metadatamapping.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This field is a collection of another class
 *   specified by listClass
 *   and identified in the sub-table by identityColumn
 * @author mhelmick
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HasMany {
	String listClass();
	String identityColumn();
}
