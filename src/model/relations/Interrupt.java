package model.relations;

import model.Entity;
import model.Relation;

import java.awt.*;

public class Interrupt extends Relation {

    public Interrupt(int id, Entity origin, Entity destination) {
        super(id, origin, destination);
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
