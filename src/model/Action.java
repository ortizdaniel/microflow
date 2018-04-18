package model;

import java.awt.*;

public class Action extends Element {

    private Edge parent;
    private Point start;
    private Point end;

    public Action(Edge parent, String name, Point initialPoint) {
        super(name);
        end = new Point(0, 0);
        setParent(parent);
        setStart(initialPoint);
        setEnd(new Point(start.x + 160, start.y));
    }

    /*public Action(Edge parent, Point initialPoint) {
        this(parent, "", initialPoint);
    }*/

    @Override
    protected void setBounds() {
        bounds.setBounds(start.x, start.y, length(), 15);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(STROKE_SMALL);
        g.setColor(isSelected() ? Color.GRAY : Color.BLACK);
        g.drawLine(start.x, start.y, start.x + 15, start.y + 15);
        g.drawLine(start.x + 15, start.y + 15, end.x, end.y + 15);
        g.fillOval(start.x - 4, start.y - 4, 8, 8);
        int x = (int) bounds.getBounds2D().getCenterX();
        int y = (int) bounds.getBounds2D().getCenterY();
        parent.drawCenteredText(g, x, y, name, Element.FONT_MED, null);
    }

    public void setParent(Edge parent) {
        this.parent = parent;
    }

    public void setStart(Point start) {
        this.start = parent.getNearestTo(start);
        end.x = this.start.x + length();
        end.y = this.start.y;
        setBounds();
    }

    public void setEnd(Point end) {
        if (this.end == null) this.end = new Point(0, 0);
        this.end.x = end.x;
        this.end.y = start.y;
        setBounds();
    }

    private int length() {
        return (int) Math.hypot(this.start.x - end.x, this.start.y - end.y);
    }

    public void update() {
        setStart(start);
        setEnd(new Point(start.x + 160, start.y));
    }
}
