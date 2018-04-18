package controller;

import model.Action;
import model.*;
import view.ContextMenu;
import view.DrawPanel;
import view.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller extends MouseAdapter implements ActionListener {

    private final View view;
    private final Graph model;
    private Element clicked;
    private CursorDetail state;
    private Node addingEdgeFrom;
    private ContextMenu contextMenu;
    private final static String OPTIONS[] = {"Read/Write", "Write", "Read"};
    private final JFileChooser chooser;
    private boolean draggingPivot;
    private boolean draggingName;
    private Point mousePoint, delta;

    private static String fileName;

    private static final String COMMENT_HEADER = "//-----------------------------------------------------------------\n";
    private static final String TAD_H = "//TAD: ";
    private static final String DATA_H = "//DATA: ";
    private static final String AUTHOR_H = "//AUTHOR: ";
    private static final String DESCR_H = "//DESCRIPTION:\n";
    private static final String INCLUD_H = "\n//------------------------ INCLUDES -----------------------\n";
    private static final String VAR_CONST_H = "\n//------------------------ VARIABLES ----------------------";
    private static final String FUNC_H = "//------------------------ FUNCTIONS ----------------------\n";
    private static final String INIT_FUNC = "\nvoid init";
    private static final String INIT_FUNC_2 = "(void) {\n\n}\n";
    private static final String INIT_FUNC_3 = "(void);\n";
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Controller(View view) {
        this.view = view;
        model = Graph.getInstance();
        clicked = null;
        state = CursorDetail.SELECTING;
        addingEdgeFrom = null;
        contextMenu = new ContextMenu();
        contextMenu.addListener(this);
        chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("BubbleWizard file", "bwz"));
        draggingPivot = false;
        draggingName = false;
        mousePoint = new Point();
        delta = new Point();

        fileName = "Diagram 1";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        state = CursorDetail.valueOf(e.getActionCommand());
        view.changeCursor(state.getCursor());

        switch (state) {
            case UNDO:
                break;
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
                exportFile();
                break;
            case GEN_MOTOR:
                break;
            case DELETE_POPUP:
                deletePopup();
                break;
            case EDIT:
                changeClickedName();
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
                if (e.getButton() == MouseEvent.BUTTON1)
                    possibleAdd(e);
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
        c.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
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
            if (model.loadFromFile(name)) {
                fileName = chooser.getSelectedFile().getName();
                view.setTitle(fileName.substring(0, fileName.length() - 4));
            } else {
                JOptionPane.showMessageDialog(view, "Error loading file.");
            }
        }
    }

    private void saveFile() {
        if (fileName.equals("Diagram 1")) {
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

        if (!extension.equalsIgnoreCase(".bwz")) {
            path += ".bwz";
        }
        if (model.saveToFile(path)) {
            view.setTitle(fileName.replace(extension, ""));
        } else {
            JOptionPane.showMessageDialog(view, "Error saving file.");
        }

    }

    private void contextMenuHideEditButton() {
        if (clicked instanceof Node) {
            if (((Node) clicked).getType().equals(NodeType.STATE)) {
                contextMenu.showEditButton(false);
            } else {
                contextMenu.showEditButton(true);
            }
        } else if (clicked instanceof Edge) {
            if (((Edge) clicked).getType().equals(EdgeType.INTERFACE)) {
                contextMenu.showEditButton(false);
            } else {
                contextMenu.showEditButton(true);
            }
        }
    }

    private void deletePopup() {
        if (clicked != null) {
            finalDelete();
            state = CursorDetail.SELECTING;
        }
    }

    private void newFile() {
        int result = JOptionPane.showConfirmDialog(view, "Are you sure you want to create a new file?",
                "Create new diagram", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            model.deleteAll();
            fileName = "Diagram 1";
            view.setTitle(fileName);
        }
    }

    private void selecting(MouseEvent e) {
        if (clicked == null) {
            clicked = model.getElementAt(e.getPoint());
            if (clicked != null) {
                clicked.setSelected(true);
                selecting(e);
            }
        } else {
            //verificar si es  un edge y se ha clickado su nombre o pivot
            if (clicked instanceof Edge) {
                Edge edge = (Edge) clicked;
                if (edge.pivotContains(e.getPoint())) {
                    draggingPivot = true;
                    return;
                } else if (edge.nameBoundsContains(e.getPoint())) {
                    draggingName = true;
                    return;
                }
            }

            if (clicked.contains(e.getPoint())) {
                //no hacer nada, puede que ahora se vaya a mover
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
        clicked = null;
    }

    private void changeClickedName() {
        String name;
        if (clicked instanceof Node) {
            if (!((Node) clicked).getType().equals(NodeType.STATE)) {
                name = askForString("Enter a name:", clicked.getName());
                contextMenu.showEditButton(true);
                if (name != null) {
                    clicked.setName(name);
                }
            }
        } else if (clicked instanceof Edge) {
            Edge e = (Edge) clicked;
            switch (e.getType()) {
                case TRANSITION:
                case INTERRUPT:
                    name = askForString("Enter " + e.getType().name().toLowerCase() + " name:", clicked.getName());
                    if (name != null) {
                        clicked.setName(name);
                    }
                    break;
                case OPERATION:
                    int res = JOptionPane.showOptionDialog(view, "What would you like this operation to be?",
                            "Operation settings", 0, JOptionPane.QUESTION_MESSAGE, null, OPTIONS,
                            null);
                    if (res == 2) { //read
                        e.setBidirectional(false);
                        e.setAsRead();
                    } else if (res == 1) { //write
                        e.setBidirectional(false);
                        e.setAsWrite();
                    } else if (res == 0) {//read/write
                        e.setBidirectional(true);
                    }
                    break;
                case ACTION:
                    //TODO: codi que es fa en el salt
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
            view.getDrawPanel().setBackground(Color.white);                 //Save ink
            view.getDrawPanel().paint(g2);
            view.getDrawPanel().setBackground(Color.decode("#FEFEFE"));

            return Printable.PAGE_EXISTS;
        });

        try {
            pj.printDialog();
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
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
                    model.addEdge(new Edge(edgeType, addingEdgeFrom, (Node) element));
                } else {
                    model.addEdge(new Edge(edgeType, state.getNameToAdd(), addingEdgeFrom, (Node) element));
                }
            }
            view.getDrawPanel().setLineStyle(DrawPanel.NONE);
            addingEdgeFrom = null;
        }

        draggingPivot = false;
        draggingName = false;
        e.getComponent().repaint();
    }

    private void clearAllSelected() {
        model.getNodes().forEach(n -> n.setSelected(false));
        model.getEdges().forEach(n -> n.setSelected(false));
    }

    private void draggedNode(Node node, Point p) {
        //se se quita este if, las cosas se mueven mejor - no poner el if
        //if (node.contains(event.getPoint())) {
        Point npt = new Point();
        npt.setLocation(node.getCenter().x + p.x, node.getCenter().y + p.y);
        node.setCenter(npt);
        for (Edge e : model.getEdges()) {
            if (e.getN1() == node || e.getN2() == node) {
                e.update();
            }
        }
        //}
    }

    private void draggedEdge(Edge edge, Point p) {
        //lo mismo de arriba
        //if (edge.pivotContains(event.getPoint())) {
        Point npt = new Point();
        if (draggingName) {
            npt.setLocation(edge.getNamePoint().x + p.x, edge.getNamePoint().y + p.y);
            edge.setNamePoint(npt);
        } else if (draggingPivot) {
            npt.setLocation(edge.getLocation().x + p.x, edge.getLocation().y + p.y);
            edge.updatePivot(npt);
            //}
        }
    }

    private void exportFile() {
        if (model.canBeExported()) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                Date date = new Date();
                Path folder = chooser.getCurrentDirectory().toPath();

                for (Node n : model.getNodes()) {
                    if (n.getType().equals(NodeType.TAD)) {
                        //.c
                        String filePath = folder.toString() + "/T" + n.getName() + ".c";
                        String name = "T" + n.getName();

                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(COMMENT_HEADER);
                            writer.write(TAD_H);
                            writer.write(name + "\n");
                            writer.write(DESCR_H);
                            writer.write(AUTHOR_H);
                            writer.write(System.getProperty("user.name") + "\n");
                            writer.write(DATA_H);
                            writer.write(dateFormat.format(date) + "\n");
                            writer.write(COMMENT_HEADER);
                            writer.write(INCLUD_H);
                            writer.write("#include \"" + name + ".h\"\n");
                            writer.write(VAR_CONST_H);
                            for (Edge e: model.getEdges()) {
                                if (e.getN1().equals(n)) {
                                    if (e.getN2().getType().equals(NodeType.VARIABLE)) {
                                        writer.write("\n" + e.getN2().getName() + ";");
                                    }
                                } else if (e.getN2().equals(n)) {
                                    if (e.getN1().getType().equals(NodeType.VARIABLE)) {
                                        writer.write("\n" + e.getN1().getName() + ";");
                                    }
                                }
                            }
                            writer.write("\n\n");
                            writer.write(FUNC_H);
                            writer.write(INIT_FUNC + name + INIT_FUNC_2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //.h
                        filePath = folder.toString() + "/T" + n.getName() + ".h";
                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(COMMENT_HEADER);
                            writer.write(TAD_H);
                            writer.write(name + "\n");
                            writer.write(DESCR_H);
                            writer.write(AUTHOR_H);
                            writer.write(System.getProperty("user.name") + "\n");
                            writer.write(DATA_H);
                            writer.write(dateFormat.format(date) + "\n");
                            writer.write(COMMENT_HEADER);
                            writer.write(INCLUD_H);
                            for (Edge e: model.getEdges()) {
                                if (e.getN1().equals(n) && e.getN2().getType() == NodeType.TAD) {
                                    writer.write("\n#include \"T" + e.getN2().getName() + ".h\"");
                                }
                            }
                            writer.write("\n");
                            writer.write(INIT_FUNC + name + INIT_FUNC_3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        } else {
            JOptionPane.showMessageDialog(null, "TAD diagram can't be empty or with States"
                    , "Error while exporting", JOptionPane.ERROR_MESSAGE);
        }
    }

}
