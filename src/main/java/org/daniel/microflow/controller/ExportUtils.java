package org.daniel.microflow.controller;

import org.daniel.microflow.model.*;
import org.daniel.microflow.view.DiagramView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class ExportUtils {

    private static final String COMMENT_HEADER = "//---------------------------------------------------------";
    private static final String TAD_H = "// @File: ";
    private static final String DATA_H = "// @Data: ";
    private static final String AUTHOR_H = "// @Author: ";
    private static final String DESCR_H = "// @Purpose:";
    private static final String INCLUD_H = "//------------------------ INCLUDES -----------------------";
    private static final String VAR_CONST_H = "//------------------------ VARIABLES ----------------------";
    private static final String FUNC_H = "//------------------------ FUNCTIONS ----------------------";
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final String sep = System.lineSeparator();

    public static File exportSourceCode(Graph model, JFileChooser chooser, DiagramView view) {
        if (model.canBeExported(1)) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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

                        //Functions
                        ArrayList<String> alreadyExported = new ArrayList<>();
                        for (Edge e: model.getEdges()) {
                            if (e.getN2().equals(n)) {
                                if (e.getN1().getType().equals(NodeType.TAD)) {
                                    if (e.getFunctions() != null) {
                                        //Check if already exported
                                        if (alreadyExported.contains(e.getName())) continue;

                                        //Get all lines
                                        ArrayList<String> a = new ArrayList<>();
                                        String[] f = e.getFunctions().split(";");
                                        for (String x : f) {
                                            String[] aux = x.split("\n");
                                            if (aux.length > 0) {
                                                Collections.addAll(a, aux);
                                            }
                                        }

                                        //Write only function lines
                                        for (String line : a) {
                                            if (line.trim().equals("")) continue;
                                            if (line.startsWith("//")) continue;
                                            sb.append(sep).append(line).append(" {").append(sep).append(sep);
                                            sb.append("}").append(sep);
                                        }

                                        alreadyExported.add(e.getName());

                                    }
                                    break;
                                }
                            }
                        }

                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(header);
                            writer.write(sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sb.setLength(0);

                        //.h
                        filePath = folder.toString() + "/T" + n.getName() + ".h";

                        sb.append("#ifndef _").append(name.toUpperCase()).append("_H_").append(sep);
                        sb.append("#define _").append(name.toUpperCase()).append("_H_").append(sep).append(sep);
                        sb.append(INCLUD_H);
                        sb.append(sep);

                        for (Edge e: model.getEdges()) {
                            if (e.getN1().equals(n) && e.getN2().getType() == NodeType.TAD) {
                                sb.append(sep).append("#include \"T").append(e.getN2().getName()).append(".h\"");
                            }
                        }
                        sb.append(sep).append(sep).append(FUNC_H).append(sep).append(sep).append("void init");
                        sb.append(name).append("(void);").append(sep);

                        //Functions
                        alreadyExported.clear();
                        for (Edge e: model.getEdges()) {
                            if (e.getN2().equals(n)) {
                                if (e.getN1().getType().equals(NodeType.TAD)) {
                                    if (e.getFunctions() != null) {
                                        //Check if already exported
                                        if (alreadyExported.contains(e.getName())) continue;

                                        ArrayList<String> a = new ArrayList<>();
                                        String[] f = e.getFunctions().split(";");
                                        for (String x : f) {
                                            String[] aux = x.split("\n");
                                            if (aux.length > 0) {
                                                Collections.addAll(a, aux);
                                            }
                                        }

                                        for (String line : a) {
                                            if (line.trim().equals("")) continue;
                                            if (line.startsWith("//")) {
                                                sb.append(line).append(sep);
                                            } else {
                                                sb.append(sep).append(line).append(";").append(sep);
                                            }
                                        }

                                        alreadyExported.add(e.getName());
                                    }
                                }
                            }
                        }

                        sb.append(sep).append("#endif");

                        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
                            writer.write(header);
                            writer.write(sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        sb.setLength(0);

                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "TAD diagram can't be empty or with States"
                    , "Error while exporting", JOptionPane.ERROR_MESSAGE);
        }
        return chooser.getSelectedFile();
    }

    public static File exportMotor(Graph model, JFileChooser chooser, DiagramView view) {
        if (model.canBeExported(0)) {
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath() + ".c";
                String name = chooser.getSelectedFile().getName();
                StringBuilder sb = new StringBuilder();


                sb.append("void ").append(name).append("(void) {").append(sep);
                sb.append("\tstatic char state = 0;\n").append(sep).append("\tswitch(state) {").append(sep);

                String aux = sb.toString();

                sb.setLength(0);

                HashSet<String> alreadyInSwitch = new HashSet<>();
                boolean isElseIf;
                for (Node n : model.getNodes()) {
                    if (n.getType().equals(NodeType.STATE) && !alreadyInSwitch.contains(n.getName())) {
                        boolean hasCondition = false;
                        sb.append("\t\tcase ").append(n.getName()).append(":").append(sep);
                        isElseIf = false;
                        for (Edge e: model.getEdges()) {

                            if (e.getN1().equals(n)) {
                                hasCondition = true;
                                String tabs = "\t\t\t\t";
                                if (e.getName().length() == 0) {
                                    tabs = "\t\t\t";
                                } else {
                                    if (!isElseIf) {
                                        sb.append("\t\t\tif (").append(e.getName()).append(") {").append(sep);
                                        isElseIf = true;
                                    } else {
                                        sb.append("\t\t\telse if (").append(e.getName()).append(") {").append(sep);
                                    }
                                }

                                if (e.getAction() != null) {
                                    String[] actions = e.getAction().getName().split(";");
                                    for (String a : actions) {
                                        String[] l = a.split("\n");
                                        for (String b : l) {
                                            if (b.trim().length() == 0) continue;
                                            sb.append(tabs).append(b);
                                            if (b.contains("{") || b.contains("}")) {
                                                sb.append(sep);
                                            } else {
                                                sb.append(";").append(sep);
                                            }
                                        }
                                    }
                                }

                                if (e.getN1() != e.getN2()) {
                                    sb.append(tabs).append("state = ").append(e.getN2().getName()).append(";").append(sep);
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
                        alreadyInSwitch.add(n.getName());
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
        } else {
            JOptionPane.showMessageDialog(null, "State diagram can't be empty or with TADs"
                    , "Error while exporting", JOptionPane.ERROR_MESSAGE);
        }

        return chooser.getSelectedFile();
    }

    public static File exportDictionary(Graph model, JFileChooser chooser, DiagramView view) {
        if (model.canBeExported(1)) {
            StringBuilder sb = new StringBuilder();
            HashSet<String> added = new HashSet<>();
            for (Edge e : model.getEdges()) {
                if (e.getType().equals(EdgeType.INTERFACE) && !added.contains(e.getName())) {
                    sb.append("//Interface ").append(e.getName()).append(sep).append(sep).append(e.getFunctions());
                    added.add(e.getName());
                }
            }

            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(chooser.getSelectedFile().getAbsolutePath() + ".txt")) {
                    fw.write(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Dictionary cannot be empty or with states"
                    , "Error while exporting", JOptionPane.ERROR_MESSAGE);
        }

        return chooser.getSelectedFile();
    }
}
