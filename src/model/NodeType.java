package model;

import java.awt.*;

public enum NodeType {

    TAD(90, 90, new BasicStroke(3), Color.BLACK, /*new Color(255, 221, 99)*/Color.ORANGE),
    VARIABLE(80, 40, new BasicStroke(3), Color.BLACK, Color.WHITE),
    PERIPHERAL(80, 40, new BasicStroke(3), Color.BLACK, Color.YELLOW),
    STATE(45, 45, new BasicStroke(3), Color.BLACK, new Color(0.0f, 0.72f, 1.0f)),
    INTERFACE(40, 40, new BasicStroke(1), Color.BLACK, Color.WHITE);

    private int width;
    private int height;
    private Stroke outer;
    private Color outline;
    private Color fill;

    NodeType(int width, int height, Stroke outer, Color outline, Color fill) {
        this.width = width;
        this.height = height;
        this.outer = outer;
        this.outline = outline;
        this.fill = fill;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Stroke getOuter() {
        return outer;
    }

    public Color getOutline() {
        return outline;
    }

    public Color getFill() {
        return fill;
    }
}
