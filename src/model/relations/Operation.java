package model.relations;

import model.Entity;
import model.Relation;

import java.awt.*;

public class Operation extends Relation {

    public Operation(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        super(id, x, y, x2, y2, origin, destination);
    }

    @Override
    public void draw(Graphics2D g) {

    }
}
