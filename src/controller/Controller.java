package controller;

import model.*;
import view.ToolBar;
import view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller extends MouseAdapter implements ActionListener {

    private final View view;
    private final Graph model;
    private Element clicked;
    private CursorState state;

    //TODO: change
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static Image cursorImage;
    private static Cursor c;

    /* Action commands */
    public static final String NEW_FILE = "NEW_FILE";
    public static final String OPEN_FILE = "OPEN_FILE";
    public static final String SAVE_FILE = "SAVE_FILE";
    public static final String SAVE_FILE_PNG = "SAVE_FILE_PNG";
    public static final String PRINT_FILE = "PRINT_FILE";
    public static final String GEN_FILES = "GENERATE_CODE";
    public static final String GEN_MOTOR = "GENERATE_MOTOR";
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
        //TODO: cambiar tipo de cursor segun el state
        //TODO: meter todo lo de cambio de cursor en una clase para precargar las imagenes
        //TODO: si cambia el tipo de cursor se tiene qe deseleccionar qualquier cosa seleccionada
        switch(e.getActionCommand()) {
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
            case CURSOR:
                state = CursorState.SELECTING;
                view.changeCursor(Cursor.getDefaultCursor());
                break;
            case UNDO:
                break;
            case DELETE:
                state = CursorState.DELETING;
                cursorImage = toolkit.getImage(ToolBar.DELETE_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case TAD:
                state = CursorState.ADD_TAD;
                cursorImage = toolkit.getImage(ToolBar.TAD_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case VAR:
                state = CursorState.ADD_VARIABLE;
                cursorImage = toolkit.getImage(ToolBar.VAR_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case PERIPHERAL:
                state = CursorState.ADD_PERIPHERAL;
                cursorImage = toolkit.getImage(ToolBar.PERIPHERAL_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case INTERFACE:
                state = CursorState.ADD_INTERFACE;
                cursorImage = toolkit.getImage(ToolBar.INTERFACE_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case OPERATION:
                state = CursorState.ADD_OPERATION;
                cursorImage = toolkit.getImage(ToolBar.OPERATION_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case INTERRUPT:
                state = CursorState.ADD_INTERRUPT;
                cursorImage = toolkit.getImage(ToolBar.INTERRUPT_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case STATE:
                state = CursorState.ADD_STATE;
                cursorImage = toolkit.getImage(ToolBar.STATE_ICON);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case TRANSITION:
                state = CursorState.ADD_TRANSITION;
                cursorImage = toolkit.getImage(ToolBar.TRANSITION_CURSOR);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
            case ACTION:
                state = CursorState.ADD_ACTION;
                cursorImage = toolkit.getImage(ToolBar.ACTION_CURSOR);
                c = toolkit.createCustomCursor(cursorImage, new Point(0,0), "Cursor");
                view.changeCursor(c);
                break;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Node n;
        switch (state) {
            case SELECTING:
                selecting(e);
                break;
            case DELETING:
                clicked = model.getElementAt(e.getPoint());
                if (clicked != null && clicked instanceof Node) {
                    model.deleteNode((Node)clicked);
                    //TODO: borrar tambien los edge asociados a los nodos
                    clicked = null;
                } else if (clicked != null) {
                    model.deleteEdge((Edge) clicked);
                }
                break;
            case ADD_TAD:
                n = new Node(NodeType.TAD, "TAD_name", e.getPoint());
                model.addNode(n);
                break;
            case ADD_VARIABLE:
                n = new Node(NodeType.VARIABLE, "var var_name", e.getPoint());
                model.addNode(n);
                break;
            case ADD_PERIPHERAL:
                n = new Node(NodeType.PERIPHERAL, "peripheral_name", e.getPoint());
                model.addNode(n);
                break;
            case ADD_STATE:
                n = new Node(NodeType.STATE, e.getPoint());
                model.addNode(n);
                break;
            case ADD_TRANSITION:
                break;
            case ADD_INTERRUPT:
                break;
            case ADD_OPERATION:
                break;
            case ADD_INTERFACE:
                break;
            case ADD_ACTION:
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
