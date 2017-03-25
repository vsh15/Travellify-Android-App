package com.mobileappclass.travel.Modules;

import java.io.Serializable;

/**
 * Created by Careena on 12/7/16.
 */
public class AttributeBean implements Serializable {

    private String attribute;
    private  boolean selected;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AttributeBean(String attribute, boolean selected) {
        this.attribute = attribute;
        this.selected = selected;
    }
}
