package model;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;

public class Edge extends Element {

    private EdgeType type;
    private Node n1;
    private Node n2;
    private final Node originalN1;
    private final Node originalN2;

    private Point pivotPoint;
    private Rectangle pivot;
    private QuadCurve2D.Float curve;
    private boolean bidir;
    private Polygon arrow;
    private Polygon arrowBidir;
    private String extra;

    private Point namePoint;
    private Ellipse2D.Float nameBounds;

    private static int interfaceCount = 0;

    private static final int PIVOT_WIDTH = 15;
    private static final int PIVOT_HEIGHT = 15;

    public Edge(EdgeType type, String name, Node n1, Node n2) {
        super(name);
        this.type = type;
        this.n1 = n1;
        this.n2 = n2;
        originalN1 = n1;
        originalN2 = n2;
        bidir = false;
        setDefaultPivot(n1.getCenter(), n2.getCenter());
        setBounds();
        nameBounds = new Ellipse2D.Float();
        setName(name);
    }

    private void setDefaultPivot(Point p1, Point p2) {
        pivotPoint = Graph.getThirdPoint(p1, p2);
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

    public void update() {
        setBounds();
    }

    @Override
    protected void setBounds() {
        /*bounds.setBounds(
                Math.min(n1.getCenter().x, n2.getCenter().x),
                Math.min(n1.getCenter().y, n2.getCenter().y),
                Math.abs(n2.getCenter().x - n1.getCenter().x),
                Math.abs(n2.getCenter().y - n1.getCenter().y)
        );*/
        Point p0 = n1.getCenter();
        Point p2 = n2.getCenter();
        curve = new QuadCurve2D.Float(p0.x, p0.y, pivotPoint.x, pivotPoint.y, p2.x, p2.y);
        bounds = curve.getBounds();

        double distance = Math.hypot(
                n1.getCenter().getX() - n2.getCenter().getX(),
                n1.getCenter().getY() - n2.getCenter().getY()
        );

        arrow = makeArrow(distance, p0, pivotPoint, p2, n2);
        if (bidir) arrowBidir = makeArrow(distance, p2, pivotPoint, p0, n1);
    }

    private Polygon makeArrow(double distance, Point p0, Point p1, Point p2, Node dest) {
        NodeType destType = dest.getType();
        double initial = distance > 30 ? 0.60 : 0.49;
        for (double t = initial; t <= 1; t += 0.005) {
            Point actual = type.equals(EdgeType.OPERATION) ? bezierLinear(t, p1, p2) : bezierQuadratic(t, p0, p1, p2);
            //si el nodo destino es un TAD o un STATE, mirar su circulo, no su bound entero
            if ((destType.equals(NodeType.TAD) || destType.equals(NodeType.STATE)) ?
                    dest.circleContains(actual) :
                    dest.contains(actual)) {
                return getArrowFor(pivotPoint, actual);
            }
        }
        return new Polygon(); //no puedo encontrar el punto, -->>nunca se da este caso
    }

    private Point bezierLinear(double t, Point p0, Point p1) {
        return new Point(
                (int) (p0.x + t * (p1.x - p0.x)),
                (int) (p0.y + t * (p1.y - p0.y))
        );
    }

    private Point bezierQuadratic(double t, Point p0, Point p1, Point p2) {
        return new Point(
                (int) ((1 - t) * ((1 - t) * p0.x + t * p1.x) + t * ((1 - t) * p1.x + t * p2.x)),
                (int) ((1 - t) * ((1 - t) * p0.y + t * p1.y) + t * ((1 - t) * p1.y + t * p2.y))
        );
    }

    /**
     * Tomado de https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
     */
    private Polygon getArrowFor(Point p1, Point p2) {
        int dx = p2.x - p1.x, dy = p2.y - p1.y;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - 15, xn = xm, ym = 7, yn = -7, x; //15 y 7 -> ancho y largo de la flecha
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
        pivotPoint = p;
        pivot = new Rectangle(p.x - PIVOT_WIDTH / 2, p.y - PIVOT_HEIGHT / 2,
                PIVOT_WIDTH, PIVOT_HEIGHT);
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

        if (type.equals(EdgeType.OPERATION)) {
            g.drawLine(p1.x, p1.y, pivotPoint.x, pivotPoint.y);
            g.drawLine(pivotPoint.x, pivotPoint.y, p2.x, p2.y);
        } else {
            if (type.equals(EdgeType.TRANSITION)) {

            } else if (type.equals(EdgeType.INTERRUPT)) {

            } else if (type.equals(EdgeType.INTERFACE)) {

            }
            g.draw(curve);
        }

        g.setStroke(medium);
        g.fill(arrow);
        if (bidir) g.fill(arrowBidir);
    }

    public void setBidirectional(boolean bidir) {
        this.bidir = bidir;
        setBounds();
    }

    public void setExtra(String extra) {
        this.extra = extra;

    }

    @Override
    public void setName(String name) {
        Canvas c = new Canvas();
        if (type.equals(EdgeType.INTERRUPT) || type.equals(EdgeType.INTERFACE) || type.equals(EdgeType.TRANSITION)) {
            int width = c.getFontMetrics(FONT_MED).stringWidth(name);
            if (namePoint == null) {
                //namePoint = new Point(pivotPoint.x - (width / 2) - 5, pivotPoint.y - (width / 2));
            }
            /*nameBounds.setFrame(
                    namePoint.x, namePoint.y,
                    width + 10, width + 10
            );*/
        }
    }

    @Override
    public Point getLocation() {
        return pivotPoint;
    }

    public void setAsRead() {
        if (originalN1 != n1 && originalN2 != n2) {
            swapNodes();
        }
    }

    public void setAsWrite() {
        if (originalN1 == n1 && originalN2 == n2) {
            swapNodes();
        }
    }

    private void swapNodes() {
        Node temp = n2;
        n2 = n1;
        n1 = temp;
        setBounds();
    }
}
