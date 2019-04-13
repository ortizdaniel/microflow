package org.daniel.microflow.view;

import org.daniel.microflow.model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class DiagramView extends JPanel {

    private static final int MIN_WIDTH = 900;
    private static final int MIN_HEIGHT = 600;
    private final OuterView parent;
    private final DrawPanel drawPanel;
    private final ToolBar jpToolBar;
    private final MenuBar jmbMenuBar;
    private final Dimension dimension = new Dimension(MIN_WIDTH, MIN_HEIGHT);
    private final FunctionsDialogPanel functionsDialogPanel;

    public DiagramView(OuterView parent, Graph graph) {
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        this.parent = parent;
        setLayout(new BorderLayout());
        JPanel content = this;
        JPanel north = new JPanel(new BorderLayout());
        drawPanel = new DrawPanel(graph) {
            @Override
            public Dimension getPreferredSize() {
                return dimension;
            }
        };
        jpToolBar = new ToolBar();
        north.add(jpToolBar, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(drawPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane, BorderLayout.CENTER);

        jmbMenuBar = new MenuBar(this, jpToolBar);
        north.add(jmbMenuBar, BorderLayout.NORTH);

        content.add(north, BorderLayout.NORTH);

        functionsDialogPanel = new FunctionsDialogPanel();

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

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }

    public String multiLineInput(String message, String title, String initial) {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(280, 120);
            }
        };
        JTextArea text = new JTextArea(initial);
        text.setTabSize(2);
        BorderLayout bl = new BorderLayout();
        bl.setVgap(10);
        panel.setLayout(bl);
        JScrollPane scroll = new JScrollPane(text);
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        int res = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            return text.getText();
        }
        return null;
    }

    public void changeDimension() {
        BorderLayout border = new BorderLayout();
        border.setVgap(10);
        border.setHgap(10);
        JPanel panel = new JPanel(border);
        JPanel north = new JPanel();
        north.add(new JLabel("Enter the desired size (in pixels)"));
        panel.add(north, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(2, 2));
        JTextField width = new JTextField(String.valueOf(drawPanel.getSize().width));
        JTextField height = new JTextField(String.valueOf(drawPanel.getSize().height));
        width.setColumns(10);
        height.setColumns(10);
        grid.add(new JLabel("Width:"));
        grid.add(width);
        grid.add(new JLabel("Height:"));
        grid.add(height);
        panel.add(grid, BorderLayout.CENTER);
        int res = JOptionPane.showConfirmDialog(this, panel, "Size", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            dimension.setSize(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
            drawPanel.revalidate();
        }
    }

    public String editFunctionDialog(String initial) {
        functionsDialogPanel.setText(initial);

        int res = JOptionPane.showConfirmDialog(
                this, functionsDialogPanel, "Show/edit functions", JOptionPane.OK_CANCEL_OPTION
        );

        if (res == JOptionPane.OK_OPTION) {
            return functionsDialogPanel.getText();
        }

        return null;
    }

    public void addSize(int width, int height) {
        int w = dimension.width, h = dimension.height;
        dimension.setSize(w + width, h + height);
    }

    public OuterView getMainView() {
        return parent;
    }
}
