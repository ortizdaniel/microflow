package model;

public enum NodeType {

    TAD(100, 100), //TODO poner cosas para pintar
    VARIABLE(160, 60),
    PERIPHERAL(160, 60),
    STATE(55, 55),
    INTERFACE(40, 40);

    private int width;
    private int height;

    NodeType(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
