package view;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {

    private JMenu jmFile;
    private JMenu jmTools;
    private JMenu jmWindow;
    private JMenu jmHelp;

    private View view;
    private JPanel jpToolBar;

    private static final String ABOUT_MSG = "BubbleWizard\nDaniel Ortiz & Joan Gómez\n" +
            "Inspired by the original of F. Escudero\nCopyright (C) 2018";

    /* Menu File Items */
    private JMenuItem jmiNewFile;
    private JMenuItem jmiOpenFile;
    private JMenuItem jmiSave;
    private JMenuItem jmiSavePNG;
    private JMenuItem jmiPrint;
    private JMenuItem jmiGenFiles;
    private JMenuItem jmiGenMotor;

    /* Tools Items */
    private JMenuItem jmiCursor;
    private JMenuItem jmiUndo;
    private JMenuItem jmiDelete;
    private JMenuItem jmiTAD;
    private JMenuItem jmiVar;
    private JMenuItem jmiPeri;
    private JMenuItem jmiIfaceC;
    private JMenuItem jmiOpC;
    private JMenuItem jmiIntC;
    private JMenuItem jmiState;
    private JMenuItem jmiTransC;
    private JMenuItem jmiAddAction;


    public MenuBar(View view, JPanel jpToolBar) {
        this.view = view;
        this.jpToolBar = jpToolBar;

        this.setName("Options menu");

        jmFile = new JMenu("File");
        this.addFileOptions();

        jmTools = new JMenu("Tools");
        this.addToolsOptions();

        jmWindow = new JMenu("Window");
        this.addWindowOptions();

        jmHelp = new JMenu("Help");
        this.addHelpOptions();

        this.add(jmFile);
        this.add(jmTools);
        this.add(jmWindow);
        this.add(jmHelp);
    }

    private void addFileOptions() {

        /* File related options */
        jmiNewFile = new JMenuItem("New file");
        jmiOpenFile = new JMenuItem("Open file");
        jmiSave = new JMenuItem("Save");
        jmiSavePNG = new JMenuItem("Save as PNG");
        jmiPrint = new JMenuItem("Print");


        JMenuItem jmiExit = new JMenuItem("Exit");
        jmiExit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        jmFile.add(jmiNewFile);
        jmFile.add(jmiOpenFile);
        jmFile.add(jmiSave);
        jmFile.add(jmiSavePNG);
        jmFile.add(jmiPrint);
        jmFile.addSeparator();

        /* Generation options */
        jmiGenFiles = new JMenuItem("Create .c .h files");
        jmiGenMotor = new JMenuItem("Create motor");

        jmFile.add(jmiGenFiles);
        jmFile.add(jmiGenMotor);
        jmFile.addSeparator();

        /* Exit */
        jmFile.add(jmiExit);
    }

    private void addToolsOptions() {
        /* Basic Options */
        jmiCursor = new JMenuItem("Cursor");
        jmiUndo = new JMenuItem("Undo");
        jmiDelete = new JMenuItem("Delete");

        jmTools.add(jmiCursor);
        jmTools.add(jmiUndo);
        jmTools.add(jmiDelete);
        jmTools.addSeparator();

        /* TAD Tools */
        jmiTAD = new JMenuItem("TAD");
        jmiVar = new JMenuItem("Variable");
        jmiPeri = new JMenuItem("Peripheral");
        jmiIfaceC = new JMenuItem("Interface cursor");
        jmiOpC = new JMenuItem("Operation cursor");
        jmiIntC = new JMenuItem("Interrupt cursor");

        jmTools.add(jmiTAD);
        jmTools.add(jmiVar);
        jmTools.add(jmiPeri);
        jmTools.add(jmiIfaceC);
        jmTools.add(jmiOpC);
        jmTools.add(jmiIntC);
        jmTools.addSeparator();

        /* States Tools */
        jmiState = new JMenuItem("State");
        jmiTransC = new JMenuItem("Transition cursor");
        jmiAddAction = new JMenuItem("Add action");

        jmTools.add(jmiState);
        jmTools.add(jmiTransC);
        jmTools.add(jmiAddAction);
    }

    private void addWindowOptions() {
        JMenuItem jmiMinimize = new JMenuItem("Minimize");
        JMenuItem jmiMaximize = new JMenuItem("Maximize");
        JMenuItem jmiToolBar = new JMenuItem("Show/Hide Toolbar");

        /* Add actions */
        jmiMinimize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setExtendedState(JFrame.ICONIFIED);
            }
        });

        jmiMaximize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        jmiToolBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jpToolBar.isVisible()) {
                    jpToolBar.setVisible(false);
                } else {
                    jpToolBar.setVisible(true);
                }
            }
        });

        jmWindow.add(jmiMinimize);
        jmWindow.add(jmiMaximize);
        jmWindow.add(jmiToolBar);
    }


    private void addHelpOptions() {
        JMenuItem jmiAbout = new JMenuItem("About");
        jmiAbout.addActionListener(e -> {
            //TODO: pasar el icono del bubblewizard
            JOptionPane.showMessageDialog(null, ABOUT_MSG, "Credits", JOptionPane.INFORMATION_MESSAGE, null);
        });

        jmHelp.add(jmiAbout);
    }


    public void addButtonListener(ActionListener c) {
        jmiNewFile.addActionListener(c);
        jmiNewFile.setActionCommand(Controller.NEW_FILE);
        jmiOpenFile.addActionListener(c);
        jmiOpenFile.setActionCommand(Controller.OPEN_FILE);
        jmiSave.addActionListener(c);
        jmiSave.setActionCommand(Controller.SAVE_FILE);
        jmiSavePNG.addActionListener(c);
        jmiSavePNG.setActionCommand(Controller.SAVE_FILE_PNG);
        jmiPrint.addActionListener(c);
        jmiPrint.setActionCommand(Controller.PRINT_FILE);
        jmiGenFiles.addActionListener(c);
        jmiGenFiles.setActionCommand(Controller.GEN_FILES);
        jmiGenMotor.addActionListener(c);
        jmiGenMotor.setActionCommand(Controller.GEN_MOTOR);

        jmiCursor.addActionListener(c);
        jmiCursor.setActionCommand(Controller.CURSOR);
        jmiUndo.addActionListener(c);
        jmiUndo.setActionCommand(Controller.UNDO);
        jmiDelete.addActionListener(c);
        jmiDelete.setActionCommand(Controller.DELETE);

        jmiTAD.addActionListener(c);
        jmiTAD.setActionCommand(Controller.TAD);
        jmiVar.addActionListener(c);
        jmiVar.setActionCommand(Controller.VAR);
        jmiPeri.addActionListener(c);
        jmiPeri.setActionCommand(Controller.PERIPHERAL);
        jmiIfaceC.addActionListener(c);
        jmiIfaceC.setActionCommand(Controller.INTERFACE);
        jmiOpC.addActionListener(c);
        jmiOpC.setActionCommand(Controller.OPERATION);
        jmiIntC.addActionListener(c);
        jmiIntC.setActionCommand(Controller.INTERRUPT);

        jmiState.addActionListener(c);
        jmiState.setActionCommand(Controller.STATE);
        jmiTransC.addActionListener(c);
        jmiTransC.setActionCommand(Controller.TRANSITION);
        jmiAddAction.addActionListener(c);
        jmiAddAction.setActionCommand(Controller.ACTION);

    }

}

