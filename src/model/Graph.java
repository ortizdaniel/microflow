package model;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Graph {

    private List<Node> nodes;
    private List<Edge> edges;

    public Graph() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
    }

    public Element getElementAt(Point p) {
        for (Node n : nodes) {
            if (n.contains(p)) return n;
        }
        for (Edge e : edges) {
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

        if (e.getType().equals(EdgeType.CALL)) {
            int count = 0;
            for (Edge k : edges) {
                if (k.getType().equals(EdgeType.CALL)) {
                    k.setName(String.valueOf(count++));
                }
            }
            Edge.decrementInterfaceCount();
        }
    }
}
