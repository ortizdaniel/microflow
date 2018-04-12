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
    private Polygon arrowBidir;

    private static int interfaceCount = 0;

    private static final int PIVOT_WIDTH = 12;
    private static final int PIVOT_HEIGHT = 12;

    public Edge(EdgeType type, String name, Node n1, Node n2) {
        super(name);
        this.type = type;
        this.n1 = n1;
        this.n2 = n2;
        bidir = false;
        setPivot(n1.getCenter(), n2.getCenter());
        setBounds();
        arrow = new Polygon();
        arrow.addPoint(5, 0);
        arrow.addPoint(0, 12);
        arrow.addPoint(5, 12);
        arrow.addPoint(10, 12);

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
        /*bounds.setBounds(
                Math.min(n1.getCenter().x, n2.getCenter().x),
                Math.min(n1.getCenter().y, n2.getCenter().y),
                Math.abs(n2.getCenter().x - n1.getCenter().x),
                Math.abs(n2.getCenter().y - n1.getCenter().y)
        );*/
        bounds = curve.getBounds();
        double distance = Math.hypot(
                n1.getCenter().getX() - n2.getCenter().getX(),
                n1.getCenter().getY() - n2.getCenter().getY()
        );

        Point p0 = n1.getCenter();
        Point p1 = pivotPoint;
        Point p2 = n2.getCenter();

        arrow = makeArrow(distance, p0, p1, p2, n2);
        if (bidir) arrowBidir = makeArrow(distance, p2, p1, p0, n1);
    }

    private Polygon makeArrow(double distance, Point p0, Point p1, Point p2, Node destination) {
        Point last = p0;
        double initial = distance > 30 ? 0.85 : 0.70;
        for (double t = initial; t <= 1; t += 0.005) {
            Point actual = new Point(
                    (int) ((1 - t) * ((1 - t) * p0.x + t * p1.x) + t * ((1 - t) * p1.x + t * p2.x)),
                    (int) ((1 - t) * ((1 - t) * p0.y + t * p1.y) + t * ((1 - t) * p1.y + t * p2.y)));
            if (destination.contains(actual)) {
                return getArrowFor(last, actual);
            } else {
                last = actual;
            }
        }
        return new Polygon();
    }

    /**
     * https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
     */
    private Polygon getArrowFor(Point p1, Point p2) {
        int dx = p2.x - p1.x, dy = p2.y - p1.y;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - 15, xn = xm, ym = 7, yn = -7, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + p1.x;
        ym = xm * sin + ym * cos + p1.y;
        xm = x;

        x = xn * cos - yn * sin + p1.x;
        yn = xn * sin + yn * cos + p1.y;
        xn = x;

        int[] xpoints = {p2.x, (int) xm, (int) xn};
        int[] ypoints = {p2.y, (int) ym, (int) yn};

        return new Polygon(xpoints, ypoints, 3);
    }

    public void updatePivot(Point p) {
        Point p1 = n1.getCenter();
        Point p2 = n2.getCenter();
        pivotPoint = p;
        pivot = new Rectangle(p.x - PIVOT_WIDTH / 2, p.y - PIVOT_HEIGHT / 2,
                PIVOT_WIDTH, PIVOT_HEIGHT);
        curve = new QuadCurve2D.Float(p1.x, p1.y, pivotPoint.x, pivotPoint.y, p2.x, p2.y);
        setBounds();
    }

    @Override
    public void draw(Graphics2D g) {
        if (selected) {
            drawOutline(g);
            g.fill(pivot);
        }

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

        g.setStroke(medium);
        g.fill(arrow);
        if (bidir) g.fill(arrowBidir);
    }

    public void setBidirectional(boolean bidir) {
        this.bidir = bidir;
        setBounds();
    }
}
