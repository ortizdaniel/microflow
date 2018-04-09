import model.*;
import view.View;

import javax.swing.*;
import java.awt.*;

public class Main {

    /*
        TODO
        Pintar cada cosa
        ToolBar
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
        Scroll en vista

     */

    public static void main(String[] args) {
        try {

            if (System.getProperty("os.name").startsWith("Mac")) {
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Ted" );
                System.setProperty("com.apple.macos.useScreenMenuBar", "true" );
                System.setProperty("apple.laf.useScreenMenuBar", "true" );
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            View view = new View();
        });


        Graph g = Graph.getInstance();
        Node n = new Node(NodeType.TAD, new Point(0, 0));
        Node n2 = new Node(NodeType.VARIABLE, "char* cua[MAX_SIZE]",new Point(100, 100));
        Node n3 = new Node(NodeType.PERIPHERAL, "Nombre largo",new Point(200, 200));
        Node n4 = new Node(NodeType.STATE, "55", new Point(300, 300));
        Node n5 = new Node(NodeType.TAD, "Loco",new Point(400, 400));
        g.addNode(n);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.addEdge(new Edge(EdgeType.INTERFACE, n2, n3));

        for (Node nn : g.getNodes()) {
            System.out.println(nn.getName());
        }

        //g.deleteNode((Node) g.getElementAt(new Point(101, 102)));

        System.out.println();
        for (Node nn : g.getNodes()) {
            System.out.println(nn.getName() + " " + nn.getCenter().toString());
        }

    }
}
