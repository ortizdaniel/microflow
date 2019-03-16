package org.daniel.microflow.model;

import java.awt.*;
import java.util.Objects;

public class Action extends Element {

    private transient Edge parent; //dependencia circular. Produce stack overflow cuando se guarda si no es transient
    private Point start;
    private Point end;
    private Rectangle pivot;

    public Action(Edge parent, String name, Point initialPoint) {
        super(name);
        end = new Point(0, 0);
        setParent(parent);
        setStart(initialPoint);
        setEnd(new Point(start.x + 160, start.y));
    }

    @Override
    protected void setBounds() {
        //inutil, guarro, se calcula cuando se pinta
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(STROKE_SMALL);
        g.setColor(isSelected() ? Color.GRAY : Color.BLACK);
        boolean endGstart = end.x > start.x;
        if (end.x > start.x) {
            g.drawLine(start.x, start.y, start.x + 15, start.y + 15);
            g.drawLine(start.x + 15, start.y + 15, end.x, end.y + 15);
        } else {
            g.drawLine(start.x, start.y, start.x - 15, start.y + 15);
            g.drawLine(start.x - 15, start.y + 15, end.x, end.y + 15);
        }
        g.fillOval(start.x - 4, start.y - 4, 8, 8);
        if (selected) g.fill(pivot);

        Point p = endGstart ? start : end;
        Point p2 = endGstart ? end : start;
        g.setFont(FONT_MED);
        String[] lines = name.replace("\t", "    ").split("\n");
        if (lines.length > 1) {
            int linesAbove = (int) Math.ceil(lines.length / 2.0);
            FontMetrics metrics = g.getFontMetrics();
            int height = metrics.getAscent();

            int y1 = p.y - (height * linesAbove) + 11;
            int y2 = p.y + (height * (lines.length - linesAbove)) + 11;

            bounds.setBounds(p.x, y1, p2.x - p.x, y2 - y1); //porquería
            for (int i = 0; i < lines.length; i++) {
                g.setColor(Color.BLACK);
                String line = lines[i];
                g.drawString(line, p.x + 20, p.y - (height * (linesAbove - i - 1)) + 11);
            }
        } else {
            bounds.setBounds(p.x, p.y, p2.x - p.x, g.getFontMetrics().getAscent());
            if (endGstart)
                g.drawString(name, p.x + 20, start.y + 13);
            else
                g.drawString(name, end.x + 10, start.y + 13);
        }
    }

    public void setParent(Edge parent) {
        this.parent = parent;
    }

    public void setStart(Point start) {
        this.start = parent.getNearestTo(start);
        end.x = endX(start);
        end.y = this.start.y;
        updatePivot();
        setBounds();
    }

    private int endX(Point start) {
        boolean endLstart = end.x < start.x;
        return this.start.x + (endLstart ? -length() : length());
    }

    public void setEnd(Point end) {
        if (this.end == null) this.end = new Point(0, 0);
        this.end.x = end.x;
        this.end.y = start.y;
        updatePivot();
        setBounds();
    }

    private void updatePivot() {
        pivot = new Rectangle(end.x - 5, end.y + 10, 10, 10); //magic constants
    }

    public boolean pivotContains(Point p) {
        return pivot.contains(p);
    }

    public void setPivot(Point p) {
        setEnd(p);
    }

    private int length() {
        return (int) Math.hypot(start.x - end.x, start.y - end.y);
    }

    public void update() {
        setStart(start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;
        return Objects.equals(parent, action.parent) &&
                Objects.equals(start, action.start) &&
                Objects.equals(end, action.end) && name.equals(action.name);
    }

    public Point getStart() {
        return start;
    }
}
