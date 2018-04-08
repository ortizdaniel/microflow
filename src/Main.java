import model.Graph;
import model.Node;
import model.NodeType;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

        });

        Graph g = new Graph();
        Node n = new Node(NodeType.STATE, "", new Point(4, 4));
        Node n2 = new Node(NodeType.STATE, "", new Point(9, 4));
        Node n3 = new Node(NodeType.STATE, "", new Point(1, 4));
        Node n4 = new Node(NodeType.STATE, "", new Point(2, 4));
        Node n5 = new Node(NodeType.STATE, "", new Point(3, 4));
        g.addNode(n);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);

        for (Node nn : g.getNodes()) {
            System.out.println(nn.getName());
        }

        g.deleteNode((Node) g.getElementAt(new Point(4, 4)));

        for (Node nn : g.getNodes()) {
            System.out.println(nn.getName());
        }
    }
}
