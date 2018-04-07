package model.entities;

import model.Entity;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TAD extends Entity {

    public TAD(int id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.setStroke(dashed);
        g.fill(new Ellipse2D.Double(x, y, 80, 80));
    }
}
