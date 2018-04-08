package model;

import view.Drawable;

import java.awt.*;

public abstract class Element implements Drawable {

    protected String name;
    protected boolean selected;
    protected Rectangle bounds;

    public Element(String name) {
        this.name = name;
        bounds = new Rectangle();
    }

    protected abstract void setBounds();

    public boolean contains(Point p) {
        return bounds.contains(p);
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
