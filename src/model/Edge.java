package model;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class Edge extends Element {

    private EdgeType type;
    private Node n1;
    private Node n2;

    private Point pivotPoint;
    private Rectangle pivot; //TODO pivot
    private QuadCurve2D.Float curve;
    private boolean bidir;
    private Polygon arrow;

    private static int interfaceCount = 0;

    private static final int PIVOT_WIDTH = 8;
    private static final int PIVOT_HEIGHT = 8;

    public Edge(EdgeType type, String name, Node n1, Node n2) {
        super(name);
        this.type = type;
        this.n1 = n1;
        this.n2 = n2;
        setBounds();
        bidir = false;
        setPivot(n1.getCenter(), n2.getCenter());
        arrow = new Polygon();
        arrow.addPoint(4, 0);
        arrow.addPoint(0, 10);
        arrow.addPoint(4, 10);
        arrow.addPoint(8, 10);

        arrow.translate(50, 50);
    }

    private void setPivot(Point n1, Point n2) {
        pivotPoint = new Point((n1.x + n2.x) / 2, (n1.y + n2.y) / 2);
        updatePivot(pivotPoint);
    }

    public Edge(EdgeType type, Node n1, Node n2) {
        this(type, String.valueOf(interfaceCount++), n1, n2);
    }

    public boolean pivotContains(Point p) {
        return pivot.contains(p);
    }

    public EdgeType getType() {
        return type;
    }

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    public static void decrementInterfaceCount() {
        interfaceCount--;
    }

    @Override
    protected void setBounds() {
        bounds.setBounds(
                Math.min(n1.getCenter().x, n2.getCenter().x),
                Math.min(n1.getCenter().y, n2.getCenter().y),
                Math.abs(n2.getCenter().x - n1.getCenter().x),
                Math.abs(n2.getCenter().y - n1.getCenter().y)
        );
    }

    public void updatePivot(Point p) {
        Point p1 = n1.getCenter();
        Point p2 = n2.getCenter();
        pivotPoint = p;
        pivot = new Rectangle(p.x - PIVOT_WIDTH / 2, p.y - PIVOT_HEIGHT / 2,
                PIVOT_WIDTH, PIVOT_HEIGHT);
        curve = new QuadCurve2D.Float(p1.x, p1.y, pivotPoint.x, pivotPoint.y, p2.x, p2.y);
    }

    @Override
    public void draw(Graphics2D g) {
        if (selected) g.draw(bounds);

        g.setStroke(type.getStroke());
        g.setColor(type.getColor());

        Point p1 = n1.getCenter();
        Point p2 = n2.getCenter();
        switch (type) {
            case TRANSITION:
            case INTERRUPT:
            case INTERFACE:
                g.draw(curve);
                break;
            case OPERATION:
                g.drawLine(p1.x, p1.y, pivotPoint.x, pivotPoint.y);
                g.drawLine(pivotPoint.x, pivotPoint.y, p2.x, p2.y);
                break;
        }

        if (selected) {
            g.setStroke(DASH_SMALL);
            g.setColor(Color.BLACK);
            g.fill(pivot);
            g.draw(bounds);
        }

        g.draw(arrow);
    }
}
