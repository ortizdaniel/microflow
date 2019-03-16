package org.daniel.microflow.view;

import java.awt.*;

public interface Drawable {

    BasicStroke thin = new BasicStroke(1);
    BasicStroke medium = new BasicStroke(3);
    BasicStroke thick = new BasicStroke(5);

    BasicStroke dashed = new BasicStroke(
            3.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f,
            new float[]{10.0f}, 0.0f);

    void draw(Graphics2D g);
}
