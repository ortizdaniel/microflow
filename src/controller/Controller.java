package controller;

import model.Action;
import model.*;
import view.ContextMenu;
import view.DrawPanel;
import view.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

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
    private boolean draggingActionPivot;
    private Point mousePoint, delta;

    private static String fileName;

    private static final String COMMENT_HEADER = "//---------------------------------------------------------";
    private static final String TAD_H = "//TAD: ";
    private static final String DATA_H = "//DATA: ";
    private static final String AUTHOR_H = "//AUTHOR: ";
    private static final String DESCR_H = "//DESCRIPTION:";
    private static final String INCLUD_H = "//------------------------ INCLUDES -----------------------";
    private static final String VAR_CONST_H = "//------------------------ VARIABLES ----------------------";
    private static final String FUNC_H = "//------------------------ FUNCTIONS ----------------------";

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final FileFilter FILTER = new FileNameExtensionFilter("Microflow file (.mcf)", "mcf");
    private static final String sep = System.lineSeparator();

    public Controller(View view) {
        this.view = view;
        model = Graph.getInstance();
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
                exportFile();
                break;
            case GEN_MOTOR:
                exportMotor();
                break;
            case GEN_DICT:
                exportDictionary();
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

        if (!extension.equalsIgnoreCase(".mcf")) {
            path += ".mcf";
        }
        if (model.saveToFile(path)) {
            view.setTitle(fileName.replace(extension, ""));
        } else {
            JOptionPane.showMessageDialog(view, "Error saving file.");
        }

    }

    private void contextMenuHideEditButton() {
        contextMenu.showEditButton(true);
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
            } else if (clicked instanceof Action && ((Action) clicked).pivotContains(e.getPoint())) {
                draggingActionPivot = true;
                return;
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
            //se quita para hacer cosas de este estilo
            //if (!((Node) clicked).getType().equals(NodeType.STATE)) {
            Node n = (Node) clicked;
            name = askForString("Enter a " + (n.getType().equals(NodeType.STATE) ? "number:" :
                            (n.getType().equals(NodeType.TAD) ? "TAD name" : n.getType().name().toLowerCase()) + ":"),
                    clicked.getName(), false);
                contextMenu.showEditButton(true);
                if (name != null) {
                    clicked.setName(name);
                    clicked.holdName(true);
                    model.decrementStatesCount(n);
                }
            //}
        } else if (clicked instanceof Edge) {
            Edge e = (Edge) clicked;
            switch (e.getType()) {
                case INTERRUPT:
                case INTERFACE:
                    name = askForString("Enter " + e.getType().name().toLowerCase() + ":", clicked.getName(), false);
                    if (name != null) {
                        clicked.setName(name);
                        clicked.holdName(true);
                        model.decrementEdgesCount(e);
                    }
                    break;
                case TRANSITION:
                    name = askForString("Enter " + e.getType().name().toLowerCase() + ":", clicked.getName(), true);
                    if (name != null) {
                        clicked.setName(name);
                        clicked.holdName(true);
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
            pj.printDialog();
            pj.print();
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

    private void exportFile() {
        if (model.canBeExported(1)) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setFileFilter(null);
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                Date date = new Date();
                Path folder;
                if (System.getProperty("os.name").startsWith("Mac")) {
                    folder = chooser.getCurrentDirectory().toPath();
                } else {
                    folder = chooser.getSelectedFile().toPath();
                }

                for (Node n : model.getNodes()) {
                    if (n.getType().equals(NodeType.TAD)) {
                        //.c
                        String filePath = folder.toString() + "/T" + n.getName() + ".c";
                        String name = "T" + n.getName();
                        StringBuilder sb = new StringBuilder();
                        String header;

                        /* HEADER */
                        sb.append(COMMENT_HEADER).append(sep).append(TAD_H).append(name).append(sep);
                        sb.append(DESCR_H).append(sep).append(AUTHOR_H).append(System.getProperty("user.name"));
                        sb.append(sep).append(DATA_H).append(dateFormat.format(date)).append(sep);
                        sb.append(COMMENT_HEADER).append(sep).append(sep);

                        header = sb.toString();
                        sb.setLength(0);

                        sb.append(INCLUD_H).append(sep).append(sep).append("#include \"").append(name).append(".h\"");
                        sb.append(sep).append(sep).append(VAR_CONST_H).append(sep);

                        for (Edge e: model.getEdges()) {
                            if (e.getN1().equals(n)) {
                                if (e.getN2().getType().equals(NodeType.VARIABLE)) {
                                    sb.append(sep).append(e.getN2().getName()).append(";");
                                }
                            } else if (e.getN2().equals(n)) {
                                if (e.getN1().getType().equals(NodeType.VARIABLE)) {
                                    sb.append(sep).append(e.getN1().getName()).append(";");
                                }
                            }
                        }
                        sb.append(sep).append(sep).append(FUNC_H).append(sep).append(sep).append("void init");
                        sb.append(name).append("(void) {").append(sep).append(sep).append("}").append(sep);

                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(header);
                            writer.write(sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sb.setLength(0);

                        //.h
                        filePath = folder.toString() + "/T" + n.getName() + ".h";

                        sb.append(INCLUD_H);
                        sb.append(sep);

                        for (Edge e: model.getEdges()) {
                            if (e.getN1().equals(n) && e.getN2().getType() == NodeType.TAD) {
                                sb.append(sep).append("#include \"T").append(e.getN2().getName()).append(".h\"");
                            }
                        }
                        sb.append(sep).append(sep).append(FUNC_H).append(sep).append(sep).append("void init");
                        sb.append(name).append("(void);").append(sep);

                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(header);
                            writer.write(sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        sb.setLength(0);

                    }
                }
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setFileFilter(FILTER);
            }
        } else {
            JOptionPane.showMessageDialog(null, "TAD diagram can't be empty or with States"
                    , "Error while exporting", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportMotor() {
        if (model.canBeExported(0)) {
            chooser.setFileFilter(new FileNameExtensionFilter("C source (.c)", "c"));
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath() + ".c";
                String name = chooser.getSelectedFile().getName();
                StringBuilder sb = new StringBuilder();


                sb.append("void ").append(name).append("(void) {").append(sep);
                sb.append("\tstatic char estat = 0;\n").append(sep).append("\tswitch(estat) {").append(sep);

                String aux = sb.toString();

                sb.setLength(0);

                for (Node n : model.getNodes()) {
                    if (n.getType().equals(NodeType.STATE)) {
                        boolean hasCondition = false;
                        sb.append("\t\tcase ").append(n.getName()).append(":").append(sep);

                        for (Edge e: model.getEdges()) {
                            if (e.getN1().equals(n)) {
                                hasCondition = true;
                                String tabs = "\t\t\t\t";
                                if (e.getName().length() == 0) {
                                    tabs = "\t\t\t";
                                } else {
                                    sb.append("\t\t\tif (").append(e.getName()).append(") {").append(sep);
                                }

                                if (e.getAction() != null) {
                                    String[] actions = e.getAction().getName().split(";");
                                    for (String a : actions) {
                                        sb.append(tabs).append(a.trim()).append(";").append(sep);
                                    }
                                    sb.append(tabs).append("estat = ").append(e.getN2().getName()).append(";").append(sep);
                                } else {
                                    sb.append(tabs).append("estat = ").append(e.getN2().getName()).append(";").append(sep);
                                }
                                if (e.getName().length() > 0) {
                                    sb.append("\t\t\t}").append(sep);
                                }
                            }
                        }
                        if (!hasCondition) {
                            sb.append(sep);
                        }
                        sb.append("\t\tbreak;").append(sep);
                    }
                }
                sb.append("\t}").append(sep).append("}");

                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                    writer.write(aux);
                    writer.write(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.setLength(0);

            }
            chooser.setFileFilter(FILTER);
        }
    }

    private void exportDictionary() {
        if (model.canBeExported(1)) {
            StringBuilder sb = new StringBuilder();
            HashSet<String> added = new HashSet<>();
            for (Edge e : model.getEdges()) {
                if (e.getType().equals(EdgeType.INTERFACE) && !added.contains(e.getName())) {
                    sb.append("//Interficie ").append(e.getName()).append(sep).append(sep);
                    added.add(e.getName());
                }
            }

            chooser.setFileFilter(new FileNameExtensionFilter("Text file (.txt)", "txt"));
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(chooser.getSelectedFile().getAbsolutePath() + ".txt")) {
                    fw.write(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            chooser.setFileFilter(FILTER);
        } else {
            JOptionPane.showMessageDialog(null, "Dictionary cannot be empty or with states"
                    , "Error while exporting", JOptionPane.ERROR_MESSAGE);
        }
    }
}
