package org.daniel.microflow.model;

import java.awt.*;

public enum NodeType {

    TAD(90, 90, new BasicStroke(3), Color.BLACK, Color.decode("#FCD0A1")),
    VARIABLE(80, 30, new BasicStroke(3), Color.BLACK, Color.WHITE),
    PERIPHERAL(80, 30, new BasicStroke(3), Color.BLACK, Color.decode("#CEDADA")),
    STATE(45, 45, new BasicStroke(3), Color.BLACK, Color.decode("#C8EAD3")),
    TEXT(80, 30, new BasicStroke(1), Color.WHITE, Color.BLACK);

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
