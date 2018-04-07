package model.relations;

import model.Entity;
import model.Relation;

import java.awt.*;

public class Operation extends Relation {

    public Operation(int id, Entity origin, Entity destination) {
        super(id, origin, destination);
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
