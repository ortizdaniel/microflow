package view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class View extends JFrame {

    private static final String TITLE = "BubbleWizard";
    private static final int MIN_WIDTH = 640;
    private static final int MIN_HEIGHT = 480;

    private LinkedList<Drawable> drawables;

    //TODO toolbar
    public View() {
        drawables = new LinkedList<>();

        setTitle(TITLE);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = (JPanel) getContentPane();
        content.add(new DrawPanel(this), BorderLayout.CENTER);
        content.add(new Toolbar(), BorderLayout.NORTH);

        this.setVisible(true);
    }

    public void addDrawable(Drawable d) {
        drawables.add(d);
    }

    public LinkedList<Drawable> getDrawables() {
        return drawables;
    }
}
