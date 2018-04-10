package model;

import view.Drawable;

import java.awt.*;

public abstract class Element implements Drawable {

    protected String name;
    protected boolean selected;
    protected Rectangle bounds;

    protected static final Stroke DASH_SMALL = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

    protected static final Stroke SMALL = new BasicStroke(1);

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

    protected void drawOutline(Graphics2D g) {
        g.setStroke(SMALL);
        g.setColor(Color.GRAY);
        g.draw(bounds);
    }
}
