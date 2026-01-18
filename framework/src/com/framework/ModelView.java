package com.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * ModelView minimal pour Sprint4-bis.
 * Contient uniquement le nom de la vue (JSP/HTML) vers laquelle forward.
 */
public class ModelView {
    private String view;
    private Map<String, Object> data = new HashMap<>();

    public ModelView() {}

    public ModelView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public ModelView addObject(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public ModelView addString(String key, String value) {
        data.put(key, value);
        return this;
    }
}
