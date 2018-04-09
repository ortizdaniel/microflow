package model;

import java.awt.*;

public class Edge extends Element {

    private EdgeType type;
    private Node n1;
    private Node n2;

    private Rectangle pivot; //TODO pivot
    private boolean bidir;

    private static int interfaceCount = 0;

    public Edge(EdgeType type, String name, Node n1, Node n2) {
        super(name);
        this.type = type;
        this.n1 = n1;
        this.n2 = n2;
        setBounds();
        bidir = false;
    }

    public Edge(EdgeType type, Node n1, Node n2) {
        this(type, String.valueOf(interfaceCount++), n1, n2);
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

    @Override
    public void draw(Graphics2D g) {
        if (selected) g.draw(bounds);

        switch (type) { //TODO pintar
            case TRANSITION:
                break;
            case INTERRUPT:
                break;
            case OPERATION:
                break;
            case INTERFACE:
                break;
        }
    }
}
