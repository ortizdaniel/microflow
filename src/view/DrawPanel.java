package view;

import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class DrawPanel extends JPanel {

    private Line2D tempLine;
    private boolean drawingLine;
    private final Stroke lineStroke = new BasicStroke(1.5f);

    public DrawPanel() {
        super();
        setBackground(Color.decode("#FEFEFE"));
        tempLine = new Line2D.Double();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (drawingLine) {
            g.setStroke(lineStroke);
            g.setColor(Color.GRAY);
            g.draw(tempLine);
        }

        for (Drawable d : Graph.getInstance().getEdges()) {
            d.draw(g);
        }
        for (Drawable d : Graph.getInstance().getNodes()) {
            d.draw(g);
        }
    }

    public void setLineStart(Point p) {
        tempLine.setLine(p, tempLine.getP2());
    }

    public void setLineEnd(Point p) {
        tempLine.setLine(tempLine.getP1(), p);
    }

    public void setDrawingLine(boolean drawingLine) {
        this.drawingLine = drawingLine;
    }
}
