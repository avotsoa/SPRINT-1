package com.annotations;

import java.lang.annotation.*;

/**
 * Annotation @HandleUrl pour mapper une méthode à une URL.
 * Usage: @HandleUrl("/chemin")
 * La méthode sera appelée quand l'URL est accédée.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandleUrl {
    String value();
}
