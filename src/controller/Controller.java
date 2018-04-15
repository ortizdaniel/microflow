package controller;

import model.*;
import view.ContextMenu;
import view.DrawPanel;
import view.View;

import javax.swing.*;
import java.awt.*;
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
    private ContextMenu contextMenu;

    public Controller(View view) {
        this.view = view;
        model = Graph.getInstance();
        clicked = null;
        state = CursorDetail.SELECTING;
        addingEdgeFrom = null;
        contextMenu = new ContextMenu();
        contextMenu.addListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = CursorDetail.valueOf(e.getActionCommand());

        switch (state) {
            case UNDO:
                break;
            case NEW_FILE:
                newFile();
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
            case DELETE_POPUP:
                if (clicked != null) {
                    if (clicked instanceof  Node) {
                        this.deleteFromPoint(clicked.getLocation());
                    } else {
                        this.deleteFromPoint(((Edge)clicked).getLocation());
                    }
                    state = CursorDetail.SELECTING;
                }
                break;
            case EDIT:
                if (clicked != null) {
                    if (clicked instanceof  Node) {
                        this.changeClickedName(clicked.getLocation());
                    } else {
                        this.changeClickedName(((Edge)clicked).getLocation());
                    }
                    state = CursorDetail.SELECTING;
                }
                break;
        }

        contextMenu.hideContextMenu();
        clearAllSelected();
        clicked = null;
        view.changeCursor(state.getCursor());
        view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        contextMenu.hideContextMenu();
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
                contextMenu.show(view.getDrawPanel(), e.getX(), e.getY());
            }
        }

        e.getComponent().repaint();
    }

    private void newFile() {
        int result = JOptionPane.showConfirmDialog(view, "Are you sure you want to create a new file?",
                "Create new diagram", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            model.deleteAll();
        }
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
        deleteFromPoint(e.getPoint());
    }

    private void deleteFromPoint(Point p) {
        clicked = model.getElementAt(p);
        if (clicked instanceof Node) {
            model.deleteNode((Node) clicked);
            clicked = null;
        } else if (clicked != null) {
            model.deleteEdge((Edge) clicked);
        }
    }

    private void changeClickedName(Point p) {
        String name;
        if (clicked instanceof Node) {
            if (!((Node) clicked).getType().equals(NodeType.STATE)) {
                name = askForString("Enter a name:", clicked.getName());
                if (name != null) {
                    model.getElementAt(p).setName(name);
                }
            }
        } else if (clicked != null) {
            switch(((Edge)clicked).getType()) {
                case TRANSITION:
                    //TODO: condicio transicio
                    break;
                case INTERRUPT:
                    name = askForString("Enter interrupt name:",  clicked.getName());
                    if (name != null) {
                        model.getElementAt(p).setName(name);
                    }
                    break;
                case OPERATION:
                    //TODO: set bidireccional
                    break;
                case INTERFACE:
                    //do nothing
                    break;
                case ACTION:
                    //TODO: codi que es fa en el salt
                    break;
            }
        }
    }

    private String askForString(String msg, String hint) {
        String s = "";
        while (s.isEmpty()) {
            s = JOptionPane.showInputDialog(msg, hint);
            if (s == null) break;
            if (s.trim().length() == 0) s = "";
        }
        return s;
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
            Component c = e.getComponent(); //DrawPanel instance
            System.out.println(c.contains(e.getPoint()));
            if (!c.contains(e.getPoint())) {
                //TODO scrollear
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
