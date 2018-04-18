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

    public View() {
        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = (JPanel) getContentPane();
        drawPanel = new DrawPanel();
        //content.add(drawPanel, BorderLayout.CENTER);
        jpToolBar = new ToolBar();
        content.add(jpToolBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(drawPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
        super.setTitle("BubbleWizard - " + title);
    }

    public String multiLineInput(String message, String title, String initial) {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(280, 120);
            }
        };
        JTextArea text = new JTextArea(initial);
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
}
