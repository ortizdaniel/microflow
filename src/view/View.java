package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import static view.ToolBar.TAD_ICON;

public class View extends JFrame {

    private static final String TITLE = "BubbleWizard";
    private static final int MIN_WIDTH = 640;
    private static final int MIN_HEIGHT = 480;
    private final DrawPanel drawPanel;
    private final ToolBar jpToolBar;
    private final MenuBar jmbMenuBar;

    public View() {
        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        JPanel content = (JPanel) getContentPane();
        drawPanel = new DrawPanel();
        content.add(drawPanel, BorderLayout.CENTER);
        jpToolBar = new ToolBar();
        content.add(jpToolBar, BorderLayout.NORTH);

        jmbMenuBar = new MenuBar(this, jpToolBar);
        this.setJMenuBar(jmbMenuBar);

        this.setVisible(true);
    }

    public void registerController(MouseAdapter ma) {
        drawPanel.addMouseListener(ma);
        drawPanel.addMouseMotionListener(ma);
    }

    public void addActionListener(ActionListener c) {
        jpToolBar.addButtonListener(c);
        jmbMenuBar.addButtonListener(c);
    }

    public void changeCursor(Cursor cursor) {
        drawPanel.setCursor(cursor);
    }


}
