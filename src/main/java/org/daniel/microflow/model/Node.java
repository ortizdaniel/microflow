package org.daniel.microflow.model;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class Node extends Element {

    protected Point center;
    protected NodeType type;

    private static int stateCount = 0;

    private static final String TAD = "TAD";

    public Node(NodeType type, String name, Point center) {
        super(name);
        this.type = type;
        this.center = center;
        setBounds();
        setName(name);
    }

    public Node(NodeType type, Point center) {
        this(type, String.valueOf(stateCount++), center);
    }

    @Override
    protected void setBounds() {
        bounds.setBounds(
                center.x - (type.getWidth() / 2), center.y - (type.getHeight() / 2),
                type.getWidth(), type.getHeight()
        );
    }

    public NodeType getType() {
        return type;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
        setBounds();
        setName(name);
    }

    public static void decrementStateCount() {
        stateCount--;
    }

    public static void setStateCount(int count) {
        stateCount = count;
    }

    public static int getStateCount() {
        return stateCount;
    }

    public boolean circleContains(Point p) {
        //radio^2 -> 45^2 = 2025 para un TAD
        //           22.5^2 = 506.25 para un estado
        //verificar si es circulo dado por esto tiene un punto p
        return Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2) <= (type.equals(NodeType.TAD) ? 2025 : 506.25);
    }

    @Override
    public void setName(String name) {
        Canvas c = new Canvas();
        if (type.equals(NodeType.VARIABLE) || type.equals(NodeType.PERIPHERAL)) {
            int width = c.getFontMetrics(FONT_MED).stringWidth(name);
            bounds.setBounds(
                    center.x - (width / 2) - 5, center.y - (type.getHeight() / 2),
                    width + 10, type.getHeight()
            );
        }
        super.setName(name);
    }

    @Override
    public void draw(Graphics2D g) {
        if (selected) {
            drawOutline(g);
        }

        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        g.setColor(type.getFill());

        switch (type) {
            case TAD:
            case STATE:
                g.fillOval(x, y, width, height);

                setStrokeAndColor(g);
                g.setStroke(type.getOuter());
                g.setColor(type.getOutline());
                g.drawOval(x, y, width, height);

                if (type.equals(NodeType.TAD)) {
                    g.draw(new QuadCurve2D.Float(
                            x + width / 2, y, center.x + 15, center.y - 15,
                            x + width, y + height / 2
                    ));

                    g.setFont(FONT_SMALL);
                    g.drawString(TAD, center.x - 20, center.y - 10);
                }

                break;
            case VARIABLE:
                g.setColor(type.getFill());
                g.fill(bounds);
                setStrokeAndColor(g);
                g.drawLine(x, y, x + width, y);
                g.drawLine(x, y + height, x + width, y + height);
                break;
            case PERIPHERAL:
                g.fill(bounds);

                if (selected) {
                    g.setStroke(type.getOuter());
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    setStrokeAndColor(g);
                }
                g.draw(bounds);
                break;
            /*case STATE:
                g.fill();
                break;
            case INTERFACE:
                break;*/
        }

        g.setFont(type.equals(NodeType.STATE) ? FONT_LARGE : FONT_MED);
        g.setColor(Color.BLACK);
        int nameWidth = g.getFontMetrics().stringWidth(name);
        g.drawString(name, center.x - nameWidth / 2,
                center.y + (type.equals(NodeType.STATE) ? FONT_LARGE.getSize() / 3 : FONT_MED.getSize() / 3));

        //g.drawLine(center.x, center.y, center.x, center.y);
    }

    private void setStrokeAndColor(Graphics2D g) {
        g.setStroke(type.getOuter());
        g.setColor(type.getOutline());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) return false;

        Node other = (Node) o;
        if (!other.center.equals(center)) return false;
        if (!other.type.equals(type)) return false;
        if (!other.name.equals(name)) return false;
        return other.bounds.equals(bounds);
    }
}
