import model.Graph;
import model.Node;
import model.NodeType;
import view.View;

import javax.swing.*;
import java.awt.*;

public class Main {

    /*
        TODO
        Pintar cada cosa
        Toolbar
        Seleccionar objeto
        Mover objeto
        Mover pivote de flechas
        Eliminar ???
        Controlador
        Exportar a PNG
        Generar código motores
        Guardar/cargar JSON
        Iconos custom toolbar

        Opcionales:
        Seleccion multiple
        Idiomas
        No solapar

     */

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            View view = new View();
        });


        Graph g = new Graph();
        Node n = new Node(NodeType.STATE, new Point(0, 0));
        Node n2 = new Node(NodeType.STATE, new Point(100, 100));
        Node n3 = new Node(NodeType.STATE, new Point(200, 200));
        Node n4 = new Node(NodeType.STATE, new Point(300, 300));
        Node n5 = new Node(NodeType.STATE, new Point(400, 400));
        g.addNode(n);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);

        for (Node nn : g.getNodes()) {
            System.out.println(nn.getName());
        }

        g.deleteNode((Node) g.getElementAt(new Point(101, 102)));

        System.out.println();
        for (Node nn : g.getNodes()) {
            System.out.println(nn.getName() + " " + nn.getCenter().toString());
        }

    }
}
