import com.apple.eawt.Application;
import controller.Controller;
import model.*;
import view.View;

import javax.swing.*;
import java.awt.*;

import static view.ToolBar.TAD_ICON;

public class Main {

    /*
        TODO
        Pintar cada cosa
        Seleccionar objeto
        Mover objeto
        Mover pivote de flechas
        Controlador
        Exportar a PNG
        Generar código motores
        Guardar/cargar JSON
        DEFINIR COLORES!!!! (para los botones tmb)
        refactor constantes actioncommand en el enum

        Opcionales:
        Seleccion multiple
        Idiomas
        No solapar
        Scroll en vista

     */

    private static final String LOGO_PATH = "res/img/logo.png";

    public static void main(String[] args) {
        try {
            if (System.getProperty("os.name").startsWith("Mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true" );
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BubbleWizard");
                Application application = Application.getApplication();
                Image image = Toolkit.getDefaultToolkit().getImage(LOGO_PATH);
                application.setDockIconImage(image);
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            View view = new View();
            Controller controller = new Controller(view);
            view.registerController(controller);
            view.addActionListener(controller);
        });

        /*
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
        Edge e = new Edge(EdgeType.INTERFACE, n2, n5);
        e.updatePivot(new Point(100, 300));
        e.setBidirectional(true);
        g.addEdge(e);
        //g.addEdge(new Edge(EdgeType.TRANSITION, n4, n5));

        Point p0 = new Point(100, 100);
        Point p1 = new Point(100, 300);
        Point p2 = new Point(200, 200);
        */

    }


}
