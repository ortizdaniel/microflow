package view;

import model.Graph;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    public DrawPanel() {
        super();
        setBackground(Color.decode("#FEFEFE"));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Drawable d : Graph.getInstance().getEdges()) {
            d.draw(g);
        }
        for (Drawable d : Graph.getInstance().getNodes()) {
            d.draw(g);
        }
    }
}
