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
    private final static String n = System.lineSeparator();
    private final static String TIMER_DEFAULT = "//Pre: Hi ha algun timer lliure." + n +
            "//Post: Retorna un handle per usar les funcions TiGetTics i TiResetTics. " + n +
            "//Retorna -1 si no hi ha cap timer disponible." + n +
            "char TiGetTimer (void);" + n + n +

            "//Pre: Handle ha estat retornat per TiGetTimer." + n +
            "//Post: Retorna els milisegons transcorreguts des de la crida a TiResetTics del Handle." + n +
            "unsigned int TiGetTics (unsigned char Handle);" + n + n +

            "//Pre: Handle ha estat retornat per TiGetTimer." + n +
            "//Post: Engega la temporització associada a 'Handle' i agafa la referencia temporal del sistema." +
            n + "void TiResetTics (unsigned char Handle);" + n + n;
    private final static String SIO_DEFAULT = "//Post: retorna el nombre de caràcters rebuts que no s'han recollit" + n +
            "//Retorna -1 si no hi ha cap timer disponible." + n +
            "int SiCharAvail(void);" + n + n +

            "//Pre: SiCharAvail() és major que zero" + n +
            "//Post: Treu i retorna el primer caràcter de la cua de recepció." + n +
            "char SiGetChar(void);" + n + n +

            "//Post: espera que el caràcter anterior s'hagi enviat i envia aquest." +
            n + "void SiSendChar(char c);" + n + n;
    private final static String ADC_DEFAULT = "//Post: Retorna la mostra convertida en 10 bits" + n +
            "int AdGetMostra(void);" + n + n;
    private static final String FUNC_DEFAULT = "//Pre: " + n + "//Post: " + n + "void func(void);" + n + n;

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

    public String editFunctionDialog(String initial) {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(650, 600);
            }
        };
        JTextArea text = new JTextArea(initial);
        text.setTabSize(2);
        BorderLayout bl = new BorderLayout();
        bl.setVgap(10);
        panel.setLayout(bl);
        JScrollPane scroll = new JScrollPane(text);
        panel.add(new JLabel("Edit the functions for the interface here:"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(2, 1));
        JPanel southSouth = new JPanel(new GridLayout(1, 4));
        JButton defaultTimer = new JButton("Timer"), defaultSIO = new JButton("SIO"),
                defaultADC = new JButton("ADC"), addPrePost = new JButton("Pre/Post");

        south.add(new JLabel("Add default functions:"));
        southSouth.add(addPrePost);
        southSouth.add(defaultTimer);
        southSouth.add(defaultSIO);
        southSouth.add(defaultADC);
        south.add(southSouth);
        panel.add(south, BorderLayout.SOUTH);


        defaultTimer.addActionListener(e -> text.append(TIMER_DEFAULT));
        defaultSIO.addActionListener(e -> text.append(SIO_DEFAULT));
        defaultADC.addActionListener(e -> text.append(ADC_DEFAULT));
        addPrePost.addActionListener(e -> text.append(FUNC_DEFAULT));
        int res = JOptionPane.showConfirmDialog(
                this, panel, "Show/edit functions", JOptionPane.OK_CANCEL_OPTION
        );
        if (res == JOptionPane.OK_OPTION) {
            return text.getText();
        }
        return null;
    }

    public void addSize(int width, int height) {
        int w = dimension.width, h = dimension.height;
        dimension.setSize(w + width, h + height);
    }
}
