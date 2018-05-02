package model;

import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

public class Edge extends Element {

    private EdgeType type;
    private Node n1;
    private Node n2;
    private final Node originalN1;
    private final Node originalN2;
    private String functions;

    private Action action;

    private Point pivotPoint;
    private Rectangle pivot;
    private QuadCurve2D.Float curve;
    private boolean bidir;
    private Polygon arrow;
    private Polygon arrowBidir;

    private Point namePoint;
    private Rectangle nameBounds;

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
        nameBounds = new Rectangle();
        setName(name);
        functions = "";
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

    public static void setInterfaceCount(int count) {
        interfaceCount = count;
    }

    public static int getInterfaceCount() {
        return interfaceCount;
    }

    public void update() {
        setBounds();
        if (!type.equals(EdgeType.TRANSITION))
            setNamePoint(bezierQuadratic(0.5, n1.getCenter(), pivotPoint, n2.getCenter()));
        if (action != null) action.update();
    }

    @Override
    protected void setBounds() {
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
        //double initial = distance > 30 ? 0.60 : 0.49;
        for (double t = 0; t <= 1; t += 0.005) {
            Point actual = type.equals(EdgeType.OPERATION) ? bezierLinear(t, p1, p2) : bezierQuadratic(t, p0, p1, p2);
            //si el nodo destino es un TAD o un STATE, mirar su circulo, no su bound entero
            if ((destType.equals(NodeType.TAD) || destType.equals(NodeType.STATE)) ?
                    dest.circleContains(actual) :
                    dest.contains(actual)) {
                return getArrowFor(pivotPoint, actual);
            }
        }
        return new Polygon(); //no se puede encontrar el punto, nunca se da este caso
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
        update();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(type.getStroke());
        g.setColor(isSelected() ? Color.GRAY : type.getColor());
        Point p1 = n1.getCenter();
        Point p2 = n2.getCenter();

        if (type.equals(EdgeType.OPERATION)) {
            g.drawLine(p1.x, p1.y, pivotPoint.x, pivotPoint.y);
            g.drawLine(pivotPoint.x, pivotPoint.y, p2.x, p2.y);
        } else {
            g.draw(curve);
            if (type.equals(EdgeType.TRANSITION) || type.equals(EdgeType.INTERRUPT)) {
                g.setColor(selected ? Color.GRAY : Color.BLACK);
                drawCenteredText(g, namePoint.x, namePoint.y, name, FONT_LARGE, this);
            } else if (type.equals(EdgeType.INTERFACE)) {
                g.setColor(Color.WHITE);
                g.fillOval(nameBounds.x, nameBounds.y, nameBounds.width, nameBounds.height);
                g.setColor(selected ? Color.GRAY : Color.BLACK);
                g.drawOval(nameBounds.x, nameBounds.y, nameBounds.width, nameBounds.height);
                drawCenteredText(g, namePoint.x, namePoint.y, name, FONT_LARGE, this);
            }
        }

        g.setStroke(medium);
        g.setColor(selected ? Color.GRAY : type.getColor());
        g.fill(arrow);
        if (bidir) g.fill(arrowBidir);

        if (isSelected()) {
            g.setStroke(STROKE_SMALL);
            g.setColor(Color.GRAY);
            g.fill(pivot);
        }

        if (action != null) action.draw(g);
    }

    public void setBidirectional(boolean bidir) {
        this.bidir = bidir;
        setBounds();
    }

    @Override
    public void setName(String name) {
        if (type.equals(EdgeType.INTERRUPT) || type.equals(EdgeType.INTERFACE) || type.equals(EdgeType.TRANSITION)) {
            if (namePoint == null) {
                namePoint = bezierQuadratic(0.5, n1.getCenter(), pivotPoint, n2.getCenter());
            }
            if (type.equals(EdgeType.INTERFACE)) {
                nameBounds = new Rectangle(namePoint.x - 20, namePoint.y - 20, 40, 40);
            }
        }
        for (Edge e : Graph.getInstance().getEdges()) {
            if (e != this && e.getType().equals(EdgeType.INTERFACE) && e.getName().equals(name)) {
                functions = e.functions;
            }
        }
        this.name = name;
    }

    public void setNamePoint(Point p) {
        namePoint = p;
        setName(name);
    }

    public boolean nameBoundsContains(Point p) {
        return nameBounds.contains(p);
    }

    @Override
    public Point getLocation() {
        return pivotPoint;
    }

    @Override
    public boolean contains(Point p) {
        if (nameBoundsContains(p)) return true;
        for (double t = 0; t <= 1; t += 0.005) {
            if (type.equals(EdgeType.OPERATION)) {
                Point cur = bezierLinear(t, n1.getCenter(), pivotPoint);
                Point cur2 = bezierLinear(t, pivotPoint, n2.getCenter());
                if (Math.hypot(p.x - cur.x, p.y - cur.y) <= 10 || Math.hypot(p.x - cur2.x, p.y - cur2.y) <= 10) {
                    return true;
                }
            } else {
                Point cur = bezierQuadratic(t, n1.getCenter(), pivotPoint, n2.getCenter());
                if (Math.hypot(p.x - cur.x, p.y - cur.y) <= 10) {
                    return true;
                }
            }
        }
        return false;
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

    public void setN1(Node n1) {
        this.n1 = n1;
    }

    public void setN2(Node n2) {
        this.n2 = n2;
    }

    /**
     * https://stackoverflow.com/questions/21267412/drawing-strings-inscribed-in-a-circle
     */
    protected void drawCenteredText(Graphics2D g, int x, int y, String text, Font f, Object caller) {
        g.setFont(text.length() >= 20 ? FONT_MED : f); //bueeeno...
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(text, g);

        int textHeight = (int) (rect.getHeight());
        int textWidth = (int) (rect.getWidth());

        int cornerX = x - (textWidth / 2);
        int cornerY = y - (textHeight / 2) + fm.getAscent();

        //la porquería más grande, pero es lo más sencillo
        if (caller == this && !type.equals(EdgeType.INTERFACE)) {
            nameBounds = new Rectangle(cornerX, y - textHeight / 2, textWidth, textHeight);
        }

        g.drawString(text, cornerX, cornerY);
    }

    public Point getNamePoint() {
        return namePoint;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    //únicamente para las transiciones y usado por las acciones
    public Point getNearestTo(Point p) {
        Point nearest = new Point(0, 0);
        double min = Double.MAX_VALUE;
        for (double t = 0; t <= 1; t += 0.001) {
            Point actual = bezierQuadratic(t, n1.getCenter(), pivotPoint, n2.getCenter());
            double d = Math.hypot(p.x - actual.x, p.y - actual.y);
            if (d < min) {
                min = d;
                nearest = actual;
            }
        }
        return nearest;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
        for (Edge e : Graph.getInstance().getEdges()) {
            if (e != this && e.getType().equals(EdgeType.INTERFACE) && e.getName().equals(name)) {
                e.functions = functions;
            }
        }
    }
}
