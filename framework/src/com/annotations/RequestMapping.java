package com.annotations;

import java.lang.annotation.*;

/**
 * Annotation pour mapper une méthode à une URL avec une méthode HTTP spécifiée.
 * Sprint 7: Différenciation des méthodes HTTP
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value() default "";
    String method() default "GET"; // GET, POST, PUT, DELETE, etc.
}

