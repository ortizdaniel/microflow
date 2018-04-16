package model;

import com.google.gson.Gson;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {

    private static final double S60 = Math.sin(60 * Math.PI / 180.0);
    private static final double C60 = Math.cos(60 * Math.PI / 180.0);
    private static final double S120 = Math.sin(30 * Math.PI / 180.0);
    private static final double C120 = Math.cos(30 * Math.PI / 180.0);

    private static Graph instance;
    private static final Gson gson = new Gson();

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;

    public static Graph getInstance() {
        if (instance == null) instance = new Graph();
        return instance;
    }

    private Graph() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public Element getElementAt(Point p) {
        for (Iterator<Node> i = nodes.descendingIterator(); i.hasNext(); ) {
            Node n = i.next();
            if (n.contains(p)) return n;
        }
        for (Iterator<Edge> i = edges.descendingIterator(); i.hasNext(); ) {
            Edge e = i.next();
            if (e.contains(p)) return e;
        }
        return null;
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void deleteNode(Node n) {
        for (Iterator<Edge> i = edges.iterator(); i.hasNext(); ) {
            Edge e = i.next();
            i.remove();
            if (e.getN1() == n || e.getN2() == n) {
                decrementEdgesCount(e);
            }
        }

        nodes.remove(n);
        if (n.getType().equals(NodeType.STATE)) {
            int count = 0;
            for (Node k : nodes) {
                if (k.getType().equals(NodeType.STATE)) {
                    k.setName(String.valueOf(count++));
                }
            }
            Node.decrementStateCount();
        }
    }

    public void decrementEdgesCount(Edge e) {
        if (e.getType().equals(EdgeType.INTERFACE)) {
            int count = 0;
            for (Edge k : edges) {
                if (k.getType().equals(EdgeType.INTERFACE)) {
                    k.setName(String.valueOf(count++));
                }
            }
            Edge.decrementInterfaceCount();
        }
    }

    public void deleteAll() {
        nodes.clear();
        edges.clear();
    }

    /**
     * https://stackoverflow.com/questions/2861904/how-to-find-coordinates-of-a-2d-equilateral-triangle-in-c
     */
    public static Point getThirdPoint(Point p1, Point p2) {
        double d = Math.hypot(p1.x - p2.x, p1.y - p2.y);
        if (d > 30) {
            return new Point(
                    //TODO hacerlo menos curvado
                    (int) (C120 * (p1.x - p2.x) - S120 * (p1.y - p2.y) + p2.x),
                    (int) (S120 * (p1.x - p2.x) + C120 * (p1.y - p2.y) + p2.y)
            );
        } else {
            return new Point(
                    (int) (C60 * (p1.x - p2.x) - S60 * (p1.y - p2.y) + p2.x),
                    (int) (S60 * (p1.x - p2.x) + C60 * (p1.y - p2.y) + p2.y)
            );
        }
    }

    public boolean loadFromFile(String path) {
        try {
            Graph g = gson.fromJson(new FileReader(path), Graph.class);
            edges = g.edges;
            nodes = g.nodes;
            for (Edge e : edges) {
                for (Node n : nodes) {
                    if (e.getN1().equals(n)) e.setN1(n);
                    if (e.getN2().equals(n)) e.setN2(n);
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean saveToFile(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(gson.toJson(this));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
