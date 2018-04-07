package model.relations;

import model.Entity;
import model.Relation;
import model.entities.Action;

import java.awt.*;

public class Transition extends Relation {

    private Action action;

    public Transition(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        super(id, x, y, x2, y2, origin, destination);
    }

    @Override
    public void draw(Graphics2D g) {

    }

    public void setAction(Action action) {
        this.action = action;
    }
}
