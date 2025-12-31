package com.framework;

import java.lang.reflect.Method;

public class Mapping {
    private Object controller;
    private Method method;
    private String httpMethod; // GET, POST, etc. (Sprint 7)

    public Mapping(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.httpMethod = "GET"; // Par défaut GET pour compatibilité
    }

    public Mapping(Object controller, Method method, String httpMethod) {
        this.controller = controller;
        this.method = method;
        this.httpMethod = httpMethod != null ? httpMethod.toUpperCase() : "GET";
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

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod != null ? httpMethod.toUpperCase() : "GET";
    }
}
