package model;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {

    private static final double S60 = Math.sin(60 * Math.PI / 180.0);
    private static final double C60 = Math.cos(60 * Math.PI / 180.0);

    private static Graph instance;

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
        edges.forEach(e -> {
            if (e.getN1() == n || e.getN2() == n) {
                deleteEdge(e);
            }
        });

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

    public static Point getThirdPoint(Point p1, Point p2) {
        return new Point(
                (int) (C60 * (p1.x - p2.x) - S60 * (p1.y - p2.y) + p2.x),
                (int) (S60 * (p1.x - p2.x) + C60 * (p1.y - p2.y) + p2.y)
        );
    }
}
