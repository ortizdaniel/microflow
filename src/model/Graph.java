package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Graph {

    private static Graph instance;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private LinkedList<Node> nodes;
    private LinkedList<Edge> edges;
    private LinkedList<Action> actions;
    private transient LinkedList<String> phases;
    private transient int index;

    public static Graph getInstance() {
        if (instance == null) instance = new Graph();
        return instance;
    }

    private Graph() {
        nodes = new LinkedList<>();
        edges = new LinkedList<>();
        actions = new LinkedList<>();
        phases = new LinkedList<>();
        index = -1;
        addPhase();
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
        decrementStatesCount(n);
    }

    public void decrementStatesCount(Node n) {
        if (n.getType().equals(NodeType.STATE)) {
            int count = 0;
            for (Node k : nodes) {
                if (k.getType().equals(NodeType.STATE) && !k.nameHold()) {
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

    public void decrementEdgesCount(Edge e) {
        if (e.getType().equals(EdgeType.INTERFACE)) {
            int count = 0;
            for (Edge k : edges) {
                if (k.getType().equals(EdgeType.INTERFACE) && !k.nameHold()) {
                    k.setName(String.valueOf(count++));
                }
            }
            Edge.decrementInterfaceCount();
        }
    }

    public void deleteAll() {
        Edge.setInterfaceCount(0);
        Node.setStateCount(0);
        nodes.clear();
        edges.clear();
        actions.clear();
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

    public boolean loadFromJson(String json) {
        try {
            Graph g = fromJson(json);
            edges = g.edges;
            nodes = g.nodes;
            actions = g.actions;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean loadFromFile(String path) {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(path));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return loadFromJson(sb.toString());
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveToFile(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(toJson());
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public boolean canBeExported(int isTAD) {
        boolean tadFound = false;
        boolean stateFound = false;

        for (Node n : nodes) {
            if (n.getType().equals(NodeType.TAD)) {
                tadFound = true;
            } else if (n.getType().equals(NodeType.STATE)) {
                stateFound = true;
            }

            if (tadFound && stateFound) {
                return false;
            }
        }

        if (isTAD == 1) {
            return tadFound;
        } else {
            return stateFound;
        }


    }

    private String toJson() {
        return gson.toJson(this);
    }

    private Graph fromJson(String json) {
        Graph g = gson.fromJson(json, Graph.class);
        for (Edge e : g.edges) {
            for (Node n : g.nodes) {
                if (e.getN1().equals(n)) e.setN1(n);
                if (e.getN2().equals(n)) e.setN2(n);
            }
            if (e.getAction() != null) {
                int size = g.actions.size();
                for (int i = 0; i < size; i++) {
                    Action a = g.actions.get(i);
                    if (a.equals(e.getAction())) {
                        g.actions.set(i, e.getAction());
                        e.getAction().setParent(e);
                    }
                }
            }
        }
        return g;
    }

    public void addPhase() {
        phases.add(toJson());
    }

    public void undo() {
        loadFromJson(phases.getLast());
        if (phases.size() > 1) phases.removeLast();
    }
}
