package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View extends JFrame {

    private static final String TITLE = "Diagram 1";
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;
    private final DrawPanel drawPanel;
    private final ToolBar jpToolBar;
    private final MenuBar jmbMenuBar;
    private Dimension dimension = new Dimension(MIN_WIDTH, MIN_HEIGHT);

    public View() {
        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JPanel content = (JPanel) getContentPane();
        drawPanel = new DrawPanel() {
            @Override
            public Dimension getPreferredSize() {
                return dimension;
            }
        };
        jpToolBar = new ToolBar();
        content.add(jpToolBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(drawPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        content.add(scrollPane, BorderLayout.CENTER);

        jmbMenuBar = new MenuBar(this, jpToolBar);
        this.setJMenuBar(jmbMenuBar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(View.this, "Are you sure you want to quit?",
                        "Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
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

    @Override
    public void setTitle(String title) {
        super.setTitle("Microflow - " + title);
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
        north.add(new JLabel("Enter the desired size"));
        panel.add(north, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(2, 2));
        JTextField width = new JTextField(String.valueOf(drawPanel.getPreferredSize().width));
        JTextField height = new JTextField(String.valueOf(drawPanel.getPreferredSize().height));
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

    public void addSize(int width, int height) {
        int w = dimension.width, h = dimension.height;
        dimension.setSize(w + width, h + height);
    }
}
