package view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {

    private static final String TITLE = "BubbleWizard";
    private static final int MIN_WIDTH = 640;
    private static final int MIN_HEIGHT = 480;

    public View() {
        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = (JPanel) getContentPane();
        content.add(new DrawPanel(this), BorderLayout.CENTER);
        ToolBar jpToolBar = new ToolBar();
        content.add(jpToolBar, BorderLayout.NORTH);

        this.setJMenuBar(new MenuBar(this, jpToolBar));

        this.setVisible(true);
    }

}
