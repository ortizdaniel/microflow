package model;

import view.Drawable;

public abstract class Entity implements Drawable {

    protected int id;
    protected int x;
    protected int y;
    protected String name;

    public Entity(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }
}
