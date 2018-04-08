package view;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private View view;

    public DrawPanel(View view) {
        super();
        this.view = view;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Drawable d : view.getDrawables()) {
            d.draw(g);
        }
    }
}
