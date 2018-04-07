package model.relations;

import model.Entity;
import model.Manager;
import model.Relation;
import model.entities.Interface;

import java.awt.*;

public class Call extends Relation {

    private Interface iface;

    public Call(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        super(id, x, y, x2, y2, origin, destination);
        iface = Manager.createInterface(x, y); //posicion relativa a mi TODO
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
