package model.relations;

import model.Entity;
import model.Relation;
import model.entities.Action;

import java.awt.*;

public class Transition extends Relation {

    private Action action;

    public Transition(int id, Entity origin, Entity destination) {
        super(id, origin, destination);
    }

    @Override
    public void draw(Graphics2D g) {

    }

    public void setAction(Action action) {
        this.action = action;
    }
}
