package model;

import view.Drawable;

public abstract class Relation implements Drawable {

    protected Entity origin;
    protected Entity destination;

    public Relation(int id, Entity origin, Entity destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Entity getOrigin() {
        return origin;
    }

    public Entity getDestination() {
        return destination;
    }
}
