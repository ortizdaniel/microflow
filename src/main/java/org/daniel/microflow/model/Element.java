package org.daniel.microflow.model;

import org.daniel.microflow.view.Drawable;

import java.awt.*;
import java.awt.geom.Point2D;

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

    protected Point2D.Double pointOfEllipsePositive(double x, int h, int k, int r) {
        x -= r;
        return new Point2D.Double(x + h, Math.sqrt(Math.pow(r, 2) - Math.pow(x, 2)) + k);
    }

    protected Point2D.Double pointOfEllipseNegative(double x, int h, int k, int r) {
        x -= r;
        return new Point2D.Double(x + h, -Math.sqrt(Math.pow(r, 2) - Math.pow(x, 2)) + k);
    }

    protected double distanceTo(Point p0, Point p1) {
        return Math.hypot(p0.x - p1.x, p0.y - p1.y);
    }

    protected double distanceTo(Point2D.Double p0, Point p1) {
        return Math.hypot(p0.x - p1.x, p0.y - p1.y);
    }

    protected Point2D.Double multiply(Point2D.Double p0, Point2D.Double p1) {
        return new Point2D.Double(p0.getX() * p1.getX(), p0.getY() * p1.getY());
    }

    protected Point2D.Double multiply(Point2D.Double p0, int c) {
        return new Point2D.Double(p0.getX() * c, p0.getY() * c);
    }

    protected Point2D.Double divide(Point2D.Double p0, Point2D.Double p1) {
        return new Point2D.Double(p0.getX() / p1.getX(), p0.getY() / p1.getY());
    }

    protected Point2D.Double add(Point2D.Double p0, Point2D.Double p1) {
        return new Point2D.Double(p0.getX() + p1.getX(), p0.getY() + p1.getY());
    }

    protected Point2D.Double substract(Point2D.Double p0, Point2D.Double p1) {
        return new Point2D.Double(p0.getX() - p1.getX(), p0.getY() - p1.getY());
    }

    protected Point2D.Double toPoint2D(Point p) {
        return new Point2D.Double(p.x, p.y);
    }
}
