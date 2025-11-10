package com.framework;

import java.lang.reflect.Method;

/**
 * Classe Mapping: associe une URL à un contrôleur et sa méthode.
 * Contient l'instance du contrôleur et la méthode à invoquer.
 */
public class Mapping {
    private Object controller;  // Instance du contrôleur
    private Method method;      // Méthode à invoquer

    public Mapping(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
