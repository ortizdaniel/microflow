package model;

public enum Radius {

    TAD(30),
    STATE(15),
    INTERFACE(10);

    private int radius;

    Radius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}
