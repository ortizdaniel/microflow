package org.daniel.microflow.view;

import org.daniel.microflow.model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.QuadCurve2D;

public class DrawPanel extends JPanel {

    public static final int CURVE = 0;
    public static final int RECT = 1;
    public static final int NONE = 2;

    private Point start;
    private Point pivot;
    private Point end;
    private int type;
    private Rectangle bounds;
    private final Stroke lineStroke = new BasicStroke(1.5f);
    private final Graph graph;
    private final ComponentListener resizeListener;
    private Dimension dim;

    public DrawPanel(Graph graph) {
        super();
        setBackground(Color.decode("#FEFEFE"));
        type = NONE;
        bounds = new Rectangle(0, 0, getWidth() - 100, getHeight() - 100);

        resizeListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bounds.setBounds(0, 0, getWidth() - 100, getHeight() - 100);
            }
        };

        addComponentListener(resizeListener);
        this.graph = graph;
        dim = new Dimension();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setStroke(lineStroke);
        g.setColor(Color.GRAY);
        if (type == CURVE) {
            g.draw(new QuadCurve2D.Float(start.x, start.y, pivot.x, pivot.y, end.x, end.y));
        } else if (type == RECT) {
            g.drawLine(start.x, start.y, pivot.x, pivot.y);
            g.drawLine(pivot.x, pivot.y, end.x, end.y);
        }

        for (Drawable d : graph.getEdges()) {
            d.draw(g);
        }

        for (Drawable d : graph.getNodes()) {
            d.draw(g);
        }
    }

    @Override
    public boolean contains(Point p) {
        return bounds.contains(p);
    }

    public void setLineStart(Point p) {
        start = p;
    }

    public void setLineEnd(Point p) {
        end = p;
    }

    public void setLinePivot(Point p) {
        pivot = p;
    }

    public void setLineStyle(int type) {
        this.type = type;
    }

    public void addSize(int width, int height) {
        bounds = new Rectangle(bounds.x, bounds.y, bounds.width + width, bounds.height + height);
    }

    public void setNewSize(int width, int height) {
        bounds = new Rectangle(bounds.x, bounds. y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(bounds);
    }

    @Override
    public Dimension getPreferredSize() {
        dim.width = bounds.width + 100;
        dim.height = bounds.height + 100;
        return dim;
    }
}
