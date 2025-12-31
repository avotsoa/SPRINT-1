package com.framework;

/**
 * ModelView minimal pour Sprint4-bis.
 * Contient uniquement le nom de la vue (JSP/HTML) vers laquelle forward.
 */
public class ModelView {
    private String view;

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
}
