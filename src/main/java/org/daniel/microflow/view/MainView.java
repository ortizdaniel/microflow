package org.daniel.microflow.view;

import org.daniel.microflow.model.Graph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class MainView extends JFrame {

    private static final String TITLE = "Diagram 1";
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 768;

    private final JTabbedPane tabbedPane;

    public MainView() {
        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        if (System.getProperty("os.name").startsWith("Mac")) {
            tabbedPane.setUI(new BasicTabbedPaneUI() {
                private final Insets borderInsets = new Insets(0, 0, 0, 0);

                @Override
                protected Insets getContentBorderInsets(int tabPlacement) {
                    return borderInsets;
                }
            });
        }

        final JPanel content = (JPanel) getContentPane();
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        content.add(tabbedPane, BorderLayout.CENTER);

        final DiagramView view = new DiagramView(this, new Graph());
        tabbedPane.addTab(TITLE, view);
    }
}
