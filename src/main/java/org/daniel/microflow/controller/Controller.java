package org.daniel.microflow.controller;

import org.daniel.microflow.model.Action;
import org.daniel.microflow.model.*;
import org.daniel.microflow.view.ContextMenu;
import org.daniel.microflow.view.DrawPanel;
import org.daniel.microflow.view.DiagramView;
import org.daniel.microflow.view.OuterView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class Controller extends MouseAdapter implements ActionListener {

    private final DiagramView view;
    private final Graph model;
    private Element clicked;
    private CursorDetail state;
    private Node addingEdgeFrom;
    private ContextMenu contextMenu;
    private final static String OPTIONS[] = {"Read/Write", "Write", "Read"};
    private final JFileChooser chooser;
    private boolean draggingPivot;
    private boolean draggingName;
    private boolean draggingActionPivot;
    private Point mousePoint, delta;
    private long lastClick;

    private String fileName;

    public static final FileFilter FILTER = new FileNameExtensionFilter("Microflow file (.mcf)", "mcf");

    public Controller(DiagramView view, Graph graph) {
        this.view = view;
        model = graph;
        clicked = null;
        state = CursorDetail.SELECTING;
        addingEdgeFrom = null;
        contextMenu = new ContextMenu();
        contextMenu.addListener(this);
        chooser = new JFileChooser();
        chooser.setFileFilter(FILTER);
        draggingPivot = false;
        draggingName = false;
        mousePoint = new Point();
        delta = new Point();

        //https://stackoverflow.com/questions/5344823/how-can-i-listen-for-key-presses-within-java-swing-across-all-components
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                state = CursorDetail.SELECTING;
                view.changeCursor(Cursor.getDefaultCursor());
            }
            return false;
        });
        fileName = "Diagram 1";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = CursorDetail.valueOf(e.getActionCommand());
        view.changeCursor(state.getCursor());

        switch (state) {
            case NEW_FILE:
                newFile();
                break;
            case OPEN_FILE:
                openFile();
                break;
            case SAVE_FILE:
                saveFile();
                break;
            case SAVE_FILE_PNG:
                saveFilePng();
                break;
            case PRINT_FILE:
                printFile();
                break;
            case GEN_FILES:
                ExportUtils.exportFile(model, chooser, view);
                break;
            case GEN_MOTOR:
                ExportUtils.exportMotor(model, chooser, view);
                break;
            case GEN_DICT:
                ExportUtils.exportDictionary(model, chooser, view);
                break;
            case DELETE_POPUP:
                deletePopup();
                break;
            case EDIT:
                changeClickedName();
                break;
            case SHOW_EDIT_FUNCTION:
                showEditFunction();
                break;
            case UNDO:
                model.undo();
                break;
        }

        if (state.getCursor().equals(Cursor.getDefaultCursor())) {
            state = CursorDetail.SELECTING;
        }
        contextMenu.hideContextMenu();
        clearAllSelected();
        clicked = null;

        view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePoint = e.getPoint();
        contextMenu.hideContextMenu();
        switch (state) {
            case SELECTING:
                selecting(e);
                break;
            case DELETING:
                delete(e);
                break;
            default:
                if (e.getButton() == MouseEvent.BUTTON1) {
                    possibleAdd(e);
                }
                break;
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            state = CursorDetail.SELECTING;
            view.changeCursor(CursorDetail.SELECTING.getCursor());
            if (clicked != null && clicked.contains(e.getPoint()) ||
                    clicked instanceof Edge && ((Edge) clicked).nameBoundsContains(e.getPoint()) ||
                    clicked instanceof Edge && ((Edge) clicked).pivotContains(e.getPoint())) {
                contextMenuHideEditButton();
                contextMenu.show(view.getDrawPanel(), e.getX(), e.getY());
            }
        }
        e.getComponent().repaint();
    }

    /**
     * https://stackoverflow.com/questions/5655908/export-jpanel-graphics-to-png-or-gif-or-jpg
     */
    private void saveFilePng() {
        JFileChooser c = new JFileChooser();
        c.setFileFilter(new FileNameExtensionFilter("PNG (.png)", "png"));
        if (c.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            Dimension d = view.getDrawPanel().getSize();
            BufferedImage img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            view.getDrawPanel().paint(g);
            g.dispose();
            try {
                ImageIO.write(img, "png", new File(c.getSelectedFile().getAbsolutePath() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openFile() {
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            String name = chooser.getSelectedFile().getAbsolutePath();
            Graph newModel = new Graph();
            if (newModel.loadFromFile(name)) {
                fileName = chooser.getSelectedFile().getName();
                File selected = chooser.getSelectedFile();
                view.getMainView().addTabFromGraph(newModel, fileName.substring(0, fileName.indexOf('.')), selected);
                view.getMainView().goToLastTab();
                //view.setTitle(fileName.substring(0, fileName.length() - 4));
            } else {
                JOptionPane.showMessageDialog(view, "Error loading file.");
            }
        }
    }

    private void saveFile() {
        if (chooser.getSelectedFile() == null) {
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                fileName = chooser.getSelectedFile().getName();
            } else {
                return;
            }
        }

        String extension = "";
        String path = chooser.getSelectedFile().getAbsolutePath();
        int i = path.lastIndexOf('.');
        if (i > 0) {
            extension = path.substring(i);
        }

        if (!extension.equalsIgnoreCase(".mcf")) {
            path += ".mcf";
        }
        if (model.saveToFile(path)) {
            String legibleName = fileName.replace(extension, "");
            view.getMainView().setTitle("Microflow - " + legibleName);
            view.getMainView().setCurrentTabTitle(legibleName);
        } else {
            JOptionPane.showMessageDialog(view, "Error saving file.");
        }

    }

    private void contextMenuHideEditButton() {
        contextMenu.showEditButton(true);
        contextMenu.showEditFunctionButton(false);
        if (clicked instanceof Node) {
            NodeType n = ((Node) clicked).getType();
            if (n.equals(NodeType.STATE)) {
                contextMenu.setEditString("state number");
            } else if (n.equals(NodeType.TAD)) {
                contextMenu.setEditString("TAD name");
            } else {
                contextMenu.setEditString(n.name().toLowerCase() + " name");
            }
            contextMenu.showEditButton(true);
        } else if (clicked instanceof Edge) {
            EdgeType e = ((Edge) clicked).getType();
            if (e.equals(EdgeType.INTERFACE)) {
                contextMenu.setEditString("interface number");
                contextMenu.showEditFunctionButton(true);
            } else if (e.equals(EdgeType.TRANSITION)) {
                contextMenu.setEditString("condition");
            } else if (e.equals(EdgeType.OPERATION)) {
                contextMenu.setEditString("operation type");
            } else if (e.equals(EdgeType.INTERRUPT)) {
                contextMenu.setEditString("interrupt request");
            }
        } else if (clicked instanceof Action) {
            contextMenu.setEditString("code");
        }
    }

    private void deletePopup() {
        if (clicked != null) {
            finalDelete();
            state = CursorDetail.SELECTING;
        }
    }

    private void showEditFunction() {
        Edge e = (Edge) clicked;
        String content = view.editFunctionDialog(e.getFunctions());
        if (content != null) {
            //e.holdName(true);
            e.setFunctions(content);
        }

    }

    private void newFile() {
        /*int result = JOptionPane.showConfirmDialog(view, "Are you sure you want to create a new file?",
                "Create new diagram", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            model.addPhase();
            model.deleteAll();
            fileName = "Diagram 1";
            //view.setTitle(fileName);
        }*/
        OuterView main = view.getMainView();
        main.addTab();
        main.goToLastTab();
    }

    private void selecting(MouseEvent e) {
        if (clicked == null) {
            clicked = model.getElementAt(e.getPoint());
            if (clicked != null) {
                clicked.setSelected(true);
                model.addPhase();
                selecting(e);
            }
        } else {
            //verificar si es  un edge y se ha clickado su nombre o pivot
            if (clicked instanceof Edge) {
                Edge edge = (Edge) clicked;
                if (edge.pivotContains(e.getPoint())) {
                    draggingPivot = true;
                    model.addPhase();
                    return;
                } else if (edge.nameBoundsContains(e.getPoint())) {
                    draggingName = true;
                    model.addPhase();
                    //return;
                }
            } else if (clicked instanceof Action && ((Action) clicked).pivotContains(e.getPoint())) {
                draggingActionPivot = true;
                model.addPhase();
                return;
            }

            if (clicked.contains(e.getPoint())) {
                //no hacer nada, puede que ahora se vaya a mover
                if (System.currentTimeMillis() - lastClick < 200) {
                    changeClickedName();
                }
                lastClick = System.currentTimeMillis();
            } else {
                //se ha clickado fuera de un elemento, posiblemente
                if (clicked instanceof Node || clicked instanceof Action || clicked instanceof Edge && !((Edge) clicked).pivotContains(e.getPoint())
                        ) {
                    //si se clicó fuera del nodo, deseleccionarlo
                    //si se clicó fuera PERO era un edge y NO se clicó en el pivot del edge, deseleccionarlo
                    //si se clicó fuera PERO era un edge y NO se clicó en el nombre del edge, deseleccionarlo
                    clicked.setSelected(false);
                    clicked = null;
                    mousePressed(e);
                }
            }
        }
        lastClick = System.currentTimeMillis();
    }

    private void delete(MouseEvent e) {
        deleteFromPoint(e.getPoint());
    }

    private void deleteFromPoint(Point p) {
        if (clicked == null) {
            clicked = model.getElementAt(p);
        }
        if (clicked != null) {
            finalDelete();
        }
    }

    private void finalDelete() {
        if (clicked instanceof Node) {
            model.deleteNode((Node) clicked);
        } else if (clicked instanceof Edge) {
            model.deleteEdge((Edge) clicked);
        } else if (clicked instanceof Action) {
            model.deleteAction((Action) clicked);
        }
        model.addPhase();
        clicked = null;
    }

    private void changeClickedName() {
        String name;
        if (clicked instanceof Node) {
            //se quita para hacer cosas de este estilo
            //if (!((Node) clicked).getType().equals(NodeType.STATE)) {
            if (((Node) clicked).getType().equals(NodeType.TEXT)) {
                String content = view.multiLineInput("Enter the text you'd like:", "Floating text", clicked.getName());
                if (content != null) clicked.setName(content);
                clicked.setSelected(false);
                clicked = null;
            } else {
                Node n = (Node) clicked;
                name = askForString("Enter a " + (n.getType().equals(NodeType.STATE) ? "number:" :
                                (n.getType().equals(NodeType.TAD) ? "TAD name" : n.getType().name().toLowerCase()) + ":"),
                        clicked.getName(), false);
                contextMenu.showEditButton(true);
                if (name != null) {
                    model.addPhase();
                    clicked.setName(name);
                    clicked.holdName(true);
                    model.changedStateName(n);
                }
            }
            //}
        } else if (clicked instanceof Edge) {
            Edge e = (Edge) clicked;
            switch (e.getType()) {
                case INTERRUPT:
                case INTERFACE:
                    name = askForString("Enter " + e.getType().name().toLowerCase() + ":", clicked.getName(), false);
                    if (name != null) {
                        model.addPhase();
                        clicked.setName(name);
                        clicked.holdName(true);
                        model.changedInterfaceName(e);
                    }
                    break;
                case TRANSITION:
                    name = askForString("Enter " + e.getType().name().toLowerCase() + ":", clicked.getName(), true);
                    if (name != null) {
                        model.addPhase();
                        clicked.setName(name);
                        clicked.holdName(true);
                    }
                    break;
                case OPERATION:
                    int res = JOptionPane.showOptionDialog(view, "What would you like this operation to be?",
                            "Operation settings", 0, JOptionPane.QUESTION_MESSAGE, null, OPTIONS,
                            null);
                    if (res == 2) { //read
                        model.addPhase();
                        e.setBidirectional(false);
                        e.setAsRead();
                    } else if (res == 1) { //write
                        model.addPhase();
                        e.setBidirectional(false);
                        e.setAsWrite();
                    } else if (res == 0) {//read/write
                        model.addPhase();
                        e.setBidirectional(true);
                    }
                    break;
            }
        } else if (clicked instanceof Action) {
            String content = view.multiLineInput("Enter the code to execute:", "Actions", clicked.getName());
            if (content != null) clicked.setName(content);
            clicked.setSelected(false);
            clicked = null;
        }
        state = CursorDetail.SELECTING;
    }

    private void printFile() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName("Print " + fileName);

        pj.setPrintable((pg, pf, pageNum) -> {
            if (pageNum > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) pg;
            pf.setOrientation(PageFormat.LANDSCAPE);
            g2.translate(pf.getImageableX(), pf.getImageableY());

            double dw = pf.getImageableWidth();
            double dh = pf.getImageableHeight();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            double xScale = dw / screenSize.width;
            double yScale = dh / screenSize.height;
            double scale = Math.min(xScale, yScale);

            double tx = 0.0;
            double ty = 0.0;
            if (xScale > scale) {
                tx = 0.5 * (xScale - scale) * screenSize.width;
            } else {
                ty = 0.5 * (yScale - scale) * screenSize.height;
            }

            g2.translate(tx, ty);
            g2.scale(scale, scale);
            view.getDrawPanel().setBackground(Color.WHITE);                 //Save ink
            view.getDrawPanel().paint(g2);
            view.getDrawPanel().setBackground(Color.decode("#FEFEFE"));

            return Printable.PAGE_EXISTS;
        });

        try {
            if (pj.printDialog()) {
                pj.print();
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }

    }

    private String askForString(String msg, String hint, boolean canBeEmpty) {
        String s = "";
        while (s.isEmpty()) {
            s = JOptionPane.showInputDialog(msg, hint);
            if (s == null) break;
            if (s.trim().length() == 0) {
                if (canBeEmpty) return "";
                else s = "";
            }
        }
        return s;
    }

    private void possibleAdd(MouseEvent e) {
        Object obj = state.getElementToAdd();
        model.addPhase();
        if (obj instanceof NodeType) {
            NodeType nt = (NodeType) obj;
            if (nt.equals(NodeType.STATE)) {
                model.addNode(new Node(nt, e.getPoint()));
            } else {
                model.addNode(new Node(nt, state.getNameToAdd(), e.getPoint()));
            }
            Component c = e.getComponent(); //DrawPanel instance
            if (!c.contains(e.getPoint())) {
                ((DrawPanel) c).addSize(30, 30);
                view.addSize(30, 30);
                c.revalidate();
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
        } else if (obj.equals(Action.class)) {
            Element element = model.getElementAt(e.getPoint());
            if (element instanceof Edge) {
                Edge edge = (Edge) element;
                Action action = new Action(edge, state.getNameToAdd(), e.getPoint());
                edge.setAction(action);
                model.addAction(action);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (state.equals(CursorDetail.SELECTING)) {
            if (clicked != null) {
                delta.setLocation(e.getX() - mousePoint.x, e.getY() - mousePoint.y);
                if (clicked instanceof Node) {
                    draggedNode((Node) clicked, delta);
                } else if (clicked instanceof Edge) {
                    draggedEdge((Edge) clicked, delta);
                } else if (clicked instanceof Action) {
                    draggedAction((Action) clicked, e, delta);
                }
                mousePoint = e.getPoint();
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
                    model.addEdge(new Edge(edgeType, addingEdgeFrom, (Node) element, model));
                } else {
                    model.addEdge(new Edge(edgeType, state.getNameToAdd(), addingEdgeFrom, (Node) element, model));
                }
            }
            view.getDrawPanel().setLineStyle(DrawPanel.NONE);
            addingEdgeFrom = null;
        }

        draggingPivot = false;
        draggingName = false;
        draggingActionPivot = false;
        e.getComponent().repaint();
    }

    private void clearAllSelected() {
        model.getNodes().forEach(n -> n.setSelected(false));
        model.getEdges().forEach(n -> n.setSelected(false));
    }

    private void draggedNode(Node node, Point p) {
        Point npt = new Point();
        npt.setLocation(node.getCenter().x + p.x, node.getCenter().y + p.y);
        node.setCenter(npt);
        for (Edge e : model.getEdges()) {
            if (e.getN1() == node || e.getN2() == node) {
                e.update();
            }
        }
    }

    private void draggedEdge(Edge edge, Point p) {
        Point npt = new Point();
        if (draggingName) {
            npt.setLocation(edge.getNamePoint().x + p.x, edge.getNamePoint().y + p.y);
            edge.setNamePoint(npt);
        } else if (draggingPivot) {
            npt.setLocation(edge.getLocation().x + p.x, edge.getLocation().y + p.y);
            edge.updatePivot(npt);
        }
    }

    private void draggedAction(Action action, MouseEvent e, Point p) {
        Point npt = new Point();
        npt.setLocation(action.getStart().x + p.x, action.getStart().y + p.y);
        if (draggingActionPivot) {
            action.setPivot(e.getPoint());
        } else {
            action.setStart(npt);
            action.update();
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setChooserFile(File f) {
        chooser.setSelectedFile(f);
    }
}
