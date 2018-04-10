package controller;

import model.Edge;
import model.Element;
import model.Graph;
import model.Node;
import view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller extends MouseAdapter implements ActionListener {

    private final View view;
    private final Graph model;
    private Edge lastSelected;

    public Controller(View view) {
        this.view = view;
        model = Graph.getInstance();
        lastSelected = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override //TODO volver a implementar
    public void mousePressed(MouseEvent e) {
        Element element = model.getElementAt(e.getPoint());
        if (element != null) {
            element.setSelected(!element.isSelected());
            e.getComponent().repaint();
            if (element instanceof Node) {

            } else if (element instanceof Edge) {
                lastSelected = (Edge) element;
            }
        } else {
            model.getNodes().forEach(n -> n.setSelected(false));
            model.getEdges().forEach(n -> n.setSelected(false));
        }
        e.getComponent().repaint();
    }

    @Override  //TODO volver a implementar
    public void mouseDragged(MouseEvent e) {
        Element element = model.getElementAt(e.getPoint());
        if (element != null) {
            if (element.isSelected() && element instanceof Node) {
                Node node = (Node) element;
                Point delta = new Point(e.getX() - node.getCenter().x, e.getY() - node.getCenter().y); //TODO hacer mover bonito
                node.setCenter(new Point(node.getCenter().x + delta.x, node.getCenter().y + delta.y));
            } else if (element.isSelected() && element instanceof Edge) {
                Edge edge = (Edge) element;
                if (edge.pivotContains(e.getPoint())) {
                    edge.updatePivot(e.getPoint());
                }
            }

        } else if (lastSelected != null) {
            if (lastSelected.pivotContains(e.getPoint())) {
                lastSelected.updatePivot(e.getPoint());
            }
        }

        e.getComponent().repaint();
    }
}
