package controller;

import model.*;
import view.DrawPanel;
import view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller extends MouseAdapter implements ActionListener {

    private final View view;
    private final Graph model;
    private Element clicked;
    private CursorDetail state;
    private Node addingEdgeFrom;

    public Controller(View view) {
        this.view = view;
        model = Graph.getInstance();
        clicked = null;
        state = CursorDetail.SELECTING;
        addingEdgeFrom = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = CursorDetail.valueOf(e.getActionCommand());
        clearAllSelected();
        clicked = null;
        view.changeCursor(state.getCursor());

        switch (state) {
            case UNDO:
                break;
            case NEW_FILE:
                break;
            case OPEN_FILE:
                break;
            case SAVE_FILE:
                break;
            case SAVE_FILE_PNG:
                break;
            case PRINT_FILE:
                break;
            case GEN_FILES:
                break;
            case GEN_MOTOR:
                break;
        }
        view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (state) {
            case SELECTING:
                selecting(e);
                break;
            case DELETING:
                delete(e);
                break;
            default:
                if (e.getButton() == MouseEvent.BUTTON1)
                    possibleAdd(e);
                break;
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            state = CursorDetail.SELECTING;
            view.changeCursor(CursorDetail.SELECTING.getCursor());
            if (clicked != null && clicked.contains(e.getPoint())) {
                //TODO mostrar menu
            }
        }

        e.getComponent().repaint();
    }

    private void selecting(MouseEvent e) {
        if (clicked == null) {
            clicked = model.getElementAt(e.getPoint());
            if (clicked != null) {
                clicked.setSelected(true);
            }
        } else {
            if (clicked.contains(e.getPoint())) {
                //no hacer nada, puede que ahora se vaya a mover
            } else {
                //se ha clickado fuera de un elemento, posiblemente
                if (clicked instanceof Node || clicked instanceof Edge && !((Edge) clicked).pivotContains(e.getPoint())) {
                    //si se clicó fuera del nodo, deseleccionarlo
                    //si se clicó fuera PERO era un edge y NO se clicó en el pivot del edge, deseleccionarlo
                    clicked.setSelected(false);
                    clicked = null;
                    mousePressed(e);
                }
            }
        }
    }

    private void delete(MouseEvent e) {
        clicked = model.getElementAt(e.getPoint());
        if (clicked instanceof Node) {
            model.deleteNode((Node) clicked);
            clicked = null;
        } else if (clicked != null) {
            model.deleteEdge((Edge) clicked);
        }
    }

    private void possibleAdd(MouseEvent e) {
        Object obj = state.getElementToAdd();
        if (obj instanceof NodeType) {
            NodeType nt = (NodeType) obj;
            if (nt.equals(NodeType.STATE)) {
                model.addNode(new Node(nt, e.getPoint()));
            } else {
                model.addNode(new Node(nt, state.getNameToAdd(), e.getPoint()));
            }
        } else if (obj instanceof EdgeType) {
            Element element = model.getElementAt(e.getPoint());
            if (element instanceof Node) {
                addingEdgeFrom = (Node) element;
                view.getDrawPanel().setLineStyle(obj.equals(EdgeType.OPERATION) ? DrawPanel.RECT : DrawPanel.CURVE);
                view.getDrawPanel().setLineStart(addingEdgeFrom.getCenter());
                view.getDrawPanel().setLinePivot(Graph.getThirdPoint(addingEdgeFrom.getCenter(), e.getPoint()));
                view.getDrawPanel().setLineEnd(addingEdgeFrom.getCenter());
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (state.equals(CursorDetail.SELECTING)) {
            if (clicked != null) {
                if (clicked instanceof Node) {
                    draggedNode((Node) clicked, e);
                } else if (clicked instanceof Edge) {
                    draggedEdge((Edge) clicked, e);
                }
            }
        }

        if (addingEdgeFrom != null) {
            view.getDrawPanel().setLinePivot(Graph.getThirdPoint(addingEdgeFrom.getCenter(), e.getPoint()));
            view.getDrawPanel().setLineEnd(e.getPoint());
        }

        e.getComponent().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (addingEdgeFrom != null) {
            Element element = model.getElementAt(e.getPoint());
            if (element instanceof Node) {
                EdgeType edgeType = (EdgeType) state.getElementToAdd();
                if (edgeType.equals(EdgeType.INTERFACE)) {
                    model.addEdge(new Edge(edgeType, addingEdgeFrom, (Node) element));
                } else {
                    model.addEdge(new Edge(edgeType, "condition", addingEdgeFrom, (Node) element));
                }
            }
            view.getDrawPanel().setLineStyle(DrawPanel.NONE);
            addingEdgeFrom = null;
        }

        e.getComponent().repaint();
    }

    private void clearAllSelected() {
        model.getNodes().forEach(n -> n.setSelected(false));
        model.getEdges().forEach(n -> n.setSelected(false));
    }

    private void draggedNode(Node node, MouseEvent event) {
        //se se quita este if, las cosas se mueven mejor - no poner el if
        //if (node.contains(event.getPoint())) {
        node.setCenter(event.getPoint());
        for (Edge e : model.getEdges()) {
            if (e.getN1() == node || e.getN2() == node) {
                e.update();
            }
        }
        //}
    }

    private void draggedEdge(Edge edge, MouseEvent event) {
        //lo mismo de arriba
        //if (edge.pivotContains(event.getPoint())) {
        edge.updatePivot(event.getPoint());
        //}
    }
}
