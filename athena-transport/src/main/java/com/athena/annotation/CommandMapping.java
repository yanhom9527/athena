package com.athena.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mukong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface CommandMapping {

    String name();

    /**
     * Get brief description of the command.
     *
     * @return brief description of the command
     * @since 1.5.0
     */
    String desc();
}
