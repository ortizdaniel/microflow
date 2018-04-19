import controller.Controller;
import view.View;

import javax.swing.*;
import java.awt.*;

public class Microflow {

    /*
        TODO

    resize iconos
    arreglar input
    cambiar \n por System.lineSeparator() al exportar
    añadir el link al github en el about
    añadir estat = X en la generación


        DEFINIR COLORES!!!! (para los botones tmb)

        Opcionales:

        "Hold" nombres de estados cambiados
        Scroll en vista

     */

    private static final String LOGO_PATH = "/res/img/logo.png";

    public static void main(String[] args) {
        try {
            if (System.getProperty("os.name").startsWith("Mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true" );
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Microflow");
                com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
                application.setDockIconImage(Toolkit.getDefaultToolkit().createImage(Microflow.class.getResource(LOGO_PATH)));
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

    }
}
