package model;

import java.awt.*;

public class Node extends Element {

    private Point center;
    private NodeType type;

    private static int stateCount = 0;

    public Node(NodeType type, String name, Point center) {
        super(name);
        this.type = type;
        this.center = center;
        setBounds();
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
    }

    public static void decrementStateCount() {
        stateCount--;
    }

    @Override
    public void draw(Graphics2D g) {
        if (selected) {
            g.draw(bounds);
        }

        switch (type) { //TODO pintar
            case TAD:
                break;
            case VARIABLE:
                break;
            case PERIPHERAL:
                break;
            case STATE:
                break;
            case INTERFACE:
                break;
        }
    }
}
