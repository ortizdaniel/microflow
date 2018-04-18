package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    private static Graph instance;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;
    private LinkedList<Action> actions;

    public static Graph getInstance() {
        if (instance == null) instance = new Graph();
        return instance;
    }

    private Graph() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
        actions = new LinkedList<>();
    }

    public Element getElementAt(Point p) {
        for (Iterator<Node> i = nodes.descendingIterator(); i.hasNext(); ) {
            Node n = i.next();
            if (n.contains(p)) return n;
        }
        for (Iterator<Action> i = actions.descendingIterator(); i.hasNext(); ) {
            Action a = i.next();
            if (a.contains(p)) return a;
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

    public void addAction(Action a) {
        actions.add(a);
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
            if (e.getN1() == n || e.getN2() == n) {
                i.remove();
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

    public void deleteEdge(Edge e) {
        edges.remove(e);
        decrementEdgesCount(e);
    }

    public void deleteAction(Action a) {
        for (Edge e : edges) {
            if (e.getAction() == a) {
                e.setAction(null);
            }
        }
    }

    private void decrementEdgesCount(Edge e) {
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
     * Where the magic happens
     */
    public static Point getThirdPoint(Point p1, Point p2) {
        Point tmp = p1;
        p1 = p2;
        p2 = tmp;
        int dx = p2.x - p1.x, dy = p2.y - p1.y;
        double angle = Math.atan2(dy, dx);
        double x1 = Math.cos(angle + (30 * Math.PI / 180.0)), y1 = Math.sin(angle + (30 * Math.PI / 180.0));
        double x2 = Math.cos(angle + (150 * Math.PI / 180.0)), y2 = Math.sin(angle + (150 * Math.PI / 180.0));
        double g1 = y1 / x1;
        double g2 = y2 / x2;

        double x = (((p2.y - g2 * p2.x) - (p1.y - g1 * p1.x)) / (g1 - g2));
        return new Point(
                (int) x,
                (int) (g1 * x + (p1.y - g1 * p1.x))
        );
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
