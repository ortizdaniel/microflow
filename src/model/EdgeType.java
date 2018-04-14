package model;

import java.awt.*;

public enum EdgeType {

    TRANSITION(new BasicStroke(1.5f), Color.BLUE),
    INTERRUPT(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0), Color.BLACK),
    OPERATION(new BasicStroke(1.5f), Color.BLUE),
    INTERFACE(new BasicStroke(1.5f), Color.BLACK),
    ACTION(new BasicStroke(1.5f), Color.BLACK);

    private Stroke stroke;
    private Color color;

    EdgeType(Stroke stroke, Color color) {
        this.stroke = stroke;
        this.color = color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public Color getColor() {
        return color;
    }
}
