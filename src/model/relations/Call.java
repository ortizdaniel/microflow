package model.relations;

import model.Entity;
import model.Relation;
import model.entities.Interface;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class Call extends Relation {

    private Interface iface;

    public Call(int id, Entity origin, Entity destination) {
        super(id, origin, destination);
        //iface = Manager.createInterface(x, y); //TODO posicion relativa a mi
    }

    @Override
    public void draw(Graphics2D g) {
        int ctrlx = (origin.getX() + destination.getX()) / 2 + 50;
        int ctrly = (origin.getY() + destination.getY()) / 2 + -50;
        g.setStroke(medium);
        g.setColor(Color.BLACK);
        g.draw(new QuadCurve2D.Double(origin.getX(), origin.getY(), ctrlx, ctrly,
                destination.getX(), destination.getY()));
        g.setStroke(thick);
        g.drawLine(ctrlx, ctrly, ctrlx, ctrly);
    }
}
