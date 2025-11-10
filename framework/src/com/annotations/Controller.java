package com.annotations;

import java.lang.annotation.*;

/**
 * Annotation @Controller pour marquer une classe comme contrôleur.
 * Usage: @Controller ou @Controller("nom")
 * Les classes annotées sont détectées automatiquement au démarrage.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    String value() default "";
}
