import controller.Controller;
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
        DEFINIR COLORES!!!!

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
            Controller controller = new Controller(view);
            view.registerController(controller);
        });

        System.out.println(System.getProperty("user.dir"));

        Graph g = Graph.getInstance();
        Node n = new Node(NodeType.TAD, new Point(0, 0));
        Node n2 = new Node(NodeType.VARIABLE, "char cua[MAX_CUA][MAX_LEGNTH]", new Point(100, 100));
        Node n3 = new Node(NodeType.PERIPHERAL, "Nombre largo",new Point(200, 200));
        Node n4 = new Node(NodeType.STATE, "55", new Point(300, 300));
        Node n5 = new Node(NodeType.TAD, "Loco",new Point(400, 400));
        g.addNode(n);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        Edge e = new Edge(EdgeType.INTERRUPT, n2, n3);
        e.updatePivot(new Point(100, 300));
        g.addEdge(e);
        //g.addEdge(new Edge(EdgeType.TRANSITION, n4, n5));


    }
}
