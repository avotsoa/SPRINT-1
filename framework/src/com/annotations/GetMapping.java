package com.annotations;

import java.lang.annotation.*;

/**
 * Annotation pour mapper une méthode à une URL avec la méthode HTTP GET.
 * Sprint 7: Différenciation des méthodes HTTP
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    String value() default "";
}

