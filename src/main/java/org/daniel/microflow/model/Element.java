package org.daniel.microflow.model;

import org.daniel.microflow.view.Drawable;

import java.awt.*;

public abstract class Element implements Drawable {

    protected String name;
    protected boolean selected;
    protected Rectangle bounds;
    private boolean holdName;

    protected static final Stroke STROKE_SMALL = new BasicStroke(1);

    protected static final Font FONT_SMALL = new Font("Calibri", Font.PLAIN, 14);
    protected static final Font FONT_MED = new Font("Calibri", Font.PLAIN, 18);
    protected static final Font FONT_LARGE = new Font("Calibri", Font.PLAIN, 24);

    public Element(String name) {
        this.name = name;
        bounds = new Rectangle();
        holdName = false;
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

    protected void drawOutline(Graphics2D g) {
        g.setStroke(STROKE_SMALL);
        g.setColor(Color.GRAY);
        g.draw(bounds);
    }

    public Point getLocation() {
        return bounds.getLocation();
    }

    public boolean nameHold() {
        return holdName;
    }

    public void holdName(boolean holdName) {
        this.holdName = holdName;
    }
}
