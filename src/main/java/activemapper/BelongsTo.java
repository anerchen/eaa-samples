package activemapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This mapped table belongs to a parent class specified by
 * value
 * 
 * The rest of the mapping relationship is inferred by a 
 * correct configuration.
 * 
 * @author mhelmick
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BelongsTo {
	String value();
}
