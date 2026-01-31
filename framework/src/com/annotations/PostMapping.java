package com.annotations;

import java.lang.annotation.*;

/**
 * Annotation pour mapper une méthode à une URL avec la méthode HTTP POST.
 * Sprint 7: Différenciation des méthodes HTTP
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostMapping {
    String value() default "";
}

