package model;

public abstract class Relation extends Entity {

    private Entity origin;
    private Entity destination;
    private int x2;
    private int y2;

    public Relation(int id, int x, int y, int x2, int y2, Entity origin, Entity destination) {
        super(id, x, y);
        this.x2 = x2;
        this.y2 = y2;
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
