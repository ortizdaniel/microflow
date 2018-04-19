package model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Objects;

public class Action extends Element {

    private transient Edge parent; //dependencia circular. Produce stack overflow si no es transient
    private Point start;
    private Point end;
    private Ellipse2D pivot;

    public Action(Edge parent, String name, Point initialPoint) {
        super(name);
        end = new Point(0, 0);
        setParent(parent);
        setStart(initialPoint);
        setEnd(new Point(start.x + 160, start.y));
    }

    @Override
    protected void setBounds() {
        bounds.setBounds(
                Math.min(start.x, end.x), start.y, length(), 15
        );
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(STROKE_SMALL);
        g.setColor(isSelected() ? Color.GRAY : Color.BLACK);
        if (end.x > start.x) {
            g.drawLine(start.x, start.y, start.x + 15, start.y + 15);
            g.drawLine(start.x + 15, start.y + 15, end.x, end.y + 15);
        } else {
            g.drawLine(start.x, start.y, start.x - 15, start.y + 15);
            g.drawLine(start.x - 15, start.y + 15, end.x, end.y + 15);
        }
        g.fillOval(start.x - 4, start.y - 4, 8, 8);
        if (selected) g.fill(pivot);
        int x = (int) bounds.getBounds2D().getCenterX();
        int y = (int) bounds.getBounds2D().getCenterY();
        parent.drawCenteredText(g, x, y, name, Element.FONT_MED, null);
        g.draw(bounds);
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
        pivot = new Ellipse2D.Float(end.x - 5, end.y + 10, 10, 10); //magic constants
    }

    public boolean pivotContains(Point p) {
        return pivot.contains(p);
    }

    public void setPivot(Point p) {
        setEnd(p);
    }

    private int length() {
        return (int) Math.hypot(this.start.x - end.x, this.start.y - end.y);
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
