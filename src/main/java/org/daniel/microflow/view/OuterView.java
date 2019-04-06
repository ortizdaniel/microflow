package org.daniel.microflow.view;

import org.daniel.microflow.controller.Controller;
import org.daniel.microflow.model.Graph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class OuterView extends JFrame {

    private static final String TITLE = "Diagram ";
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 768;

    private final JTabbedPane tabbedPane;
    private int tabId;

    public OuterView() {
        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        tabbedPane.setBackground(Color.WHITE);
        setTitle("Microflow - " + TITLE + tabId);

        if (System.getProperty("os.name").startsWith("Mac")) {
            tabbedPane.setUI(new BasicTabbedPaneUI() {
                private final Insets borderInsets = new Insets(0, 0, 0, 0);

                @Override
                protected Insets getContentBorderInsets(int tabPlacement) {
                    return borderInsets;
                }
            });
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(OuterView.this, "Are you sure you want to quit?",
                        "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });

        final JPanel content = (JPanel) getContentPane();
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        content.add(tabbedPane, BorderLayout.CENTER);

        tabId = 0;
        addTab();
        setVisible(true);
    }

    public void registerController(ChangeListener l) {
        tabbedPane.addChangeListener(l);
    }

    public void addTab() {
        addTabFromGraph(new Graph(), TITLE + tabId, null);
    }

    public void goToLastTab() {
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    public void addTabFromGraph(Graph graph, String name, File selected) {
        DiagramView view = new DiagramView(this, graph);
        Controller controller = new Controller(view, graph);
        view.registerController(controller);
        view.addActionListener(controller);
        controller.setFileName(name);
        controller.setChooserFile(selected);

        JPanel tabTitle = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));

        tabTitle.add(new JLabel(name));
        tabTitle.setBackground(Color.WHITE);

        JButton closeButton = ToolBar.makeCustomButton("Close tab", ToolBar.CLOSE_ICON, 18);
        closeButton.setActionCommand(String.valueOf(tabId));
        closeButton.addActionListener(l -> {
            JButton btn = (JButton) l.getSource();
            String toClose = btn.getActionCommand();
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                JButton other = ((JButton) ((JPanel) tabbedPane.getTabComponentAt(i)).getComponent(1));
                if (other.getActionCommand().equalsIgnoreCase(toClose)) {

                    int result = JOptionPane.showConfirmDialog(view, "Are you sure you want to close this tab?",
                            "Close tab", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        if (tabbedPane.getTabCount() == 1) {
                            System.exit(0);
                        } else {
                            tabbedPane.removeTabAt(i);
                        }
                    }
                    break;
                }
            }
        });
        closeButton.setPreferredSize(new Dimension(24, 15));
        tabTitle.add(closeButton);
        tabTitle.setBorder(new EmptyBorder(0, 0, 0, 0));

        tabbedPane.addTab(null, view);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabTitle);
        tabId++;
    }

    public void setCurrentTabTitle(String title) {
        int index = tabbedPane.getSelectedIndex();
        JPanel currentTitle = (JPanel) tabbedPane.getTabComponentAt(index);
        JLabel label = (JLabel) currentTitle.getComponent(0);
        label.setText(title);
    }
}
