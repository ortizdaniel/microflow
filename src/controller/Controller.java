package controller;

import model.Edge;
import model.Element;
import model.Graph;
import model.Node;
import view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller extends MouseAdapter implements ActionListener {

    private final View view;
    private final Graph model;
    private Element clicked;
    private CursorState state;

    /* Action commands */
    public static final String NEW_FILE = "NEW_FILE";
    public static final String OPEN_FILE = "OPEN_FILE";
    public static final String SAVE_FILE = "SAVE_FILE";
    public static final String PRINT_FILE = "PRINT_FILE";
    public static final String CURSOR = "NORMAL_CURSOR";
    public static final String UNDO = "UNDO";
    public static final String DELETE = "DELETE";
    public static final String TAD = "CREATE_TAD";
    public static final String VAR = "CREATE_VAR";
    public static final String PERIPHERAL = "CREATE_PERIPHERAL";
    public static final String INTERFACE = "INTERFACE";
    public static final String OPERATION = "OPERATION";
    public static final String INTERRUPT = "INTERRUPT";
    public static final String STATE = "STATE";
    public static final String TRANSITION = "TRANSITION";
    public static final String ACTION = "ACTION";

    public Controller(View view) {
        this.view = view;
        model = Graph.getInstance();
        clicked = null;
        state = CursorState.SELECTING;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        switch(e.getActionCommand()) {
            case NEW_FILE:
                break;
            case OPEN_FILE:
                break;
            case PRINT_FILE:
                break;
            case CURSOR:
                break;
            case UNDO:
                break;
            case DELETE:
                break;
            case TAD:
                break;
            case VAR:
                break;
            case PERIPHERAL:
                break;
            case INTERFACE:
                break;
            case OPERATION:
                break;
            case INTERRUPT:
                break;
            case STATE:
                break;
            case TRANSITION:
                break;
            case ACTION:
                break;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (state) {
            case SELECTING:
                selecting(e);
                break;
            case DELETING:
                break;
            case ADD_TAD:
                break;
            case ADD_VARIABLE:
                break;
            case ADD_PERIPHERAL:
                break;
            case ADD_STATE:
                break;
            case ADD_TRANSITION:
                break;
            case ADD_INTERRUPT:
                break;
            case ADD_OPERATION:
                break;
            case ADD_INTERFACE:
                break;
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

    @Override
    public void mouseDragged(MouseEvent e) {
        if (state.equals(CursorState.SELECTING)) {
            if (clicked != null) {
                if (clicked instanceof Node) {
                    draggedNode((Node) clicked, e);
                } else if (clicked instanceof Edge) {
                    draggedEdge((Edge) clicked, e);
                }
            }
            e.getComponent().repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

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
                return;
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
