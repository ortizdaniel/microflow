import controller.Controller;
import model.Action;
import model.*;
import view.View;

import javax.swing.*;
import java.awt.*;

public class Main {

    /*
        TODO
        Pintar cada cosa

    resize iconos
    arreglar input
    cambiar \n por System.lineSeparator()

        Controlador
        Exportar a PNG
        Generar código motores
        DEFINIR COLORES!!!! (para los botones tmb)
        refactor constantes actioncommand en el enum

        Opcionales:
        No solapar
        "Hold" nombres de estados cambiados
        Scroll en vista

     */

    public static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final String LOGO_PATH = "res/img/logo.png";

    public static void main(String[] args) {
        try {
            if (System.getProperty("os.name").startsWith("Mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true" );
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BubbleWizard");
                com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
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


        Graph g = Graph.getInstance();

        Node n = new Node(NodeType.STATE, new Point(400, 100));
        Node n2 = new Node(NodeType.STATE, new Point(400, 400));
        g.addNode(n);
        g.addNode(n2);
        Edge e = new Edge(EdgeType.TRANSITION, n, n2);
        g.addEdge(e);
        Action a = new Action(e, "LOCO", e.getLocation());
        g.addAction(a);
        e.setAction(a);
    }
}
