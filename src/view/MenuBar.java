package view;

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
        JMenuItem jmiNewFile = new JMenuItem("New file");
        JMenuItem jmiOpenFile = new JMenuItem("Open file");
        JMenuItem jmiSave = new JMenuItem("Save");
        JMenuItem jmiSavePNG = new JMenuItem("Save as PNG");
        JMenuItem jmiPrint = new JMenuItem("Print");


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
        JMenuItem jmiGenFiles = new JMenuItem("Create .c .h files");
        JMenuItem jmiGenMotor = new JMenuItem("Create motor");

        jmFile.add(jmiGenFiles);
        jmFile.add(jmiGenMotor);
        jmFile.addSeparator();

        /* Exit */
        jmFile.add(jmiExit);
    }

    private void addToolsOptions() {
        /* Basic Options */
        JMenuItem jmiUndo = new JMenuItem("Undo");

        jmTools.add(jmiUndo);
        jmTools.addSeparator();

        /* TAD Tools */
        JMenuItem jmiTAD = new JMenuItem("TAD");
        JMenuItem jmiVar = new JMenuItem("Variable");
        JMenuItem jmiPeri = new JMenuItem("Peripheral");
        JMenuItem jmiIfaceC = new JMenuItem("Interface cursor");
        JMenuItem jmiOpC = new JMenuItem("Operation cursor");
        JMenuItem jmiIntC = new JMenuItem("Interrupt cursor");

        jmTools.add(jmiTAD);
        jmTools.add(jmiVar);
        jmTools.add(jmiPeri);
        jmTools.add(jmiIfaceC);
        jmTools.add(jmiOpC);
        jmTools.add(jmiIntC);
        jmTools.addSeparator();

        /* States Tools */
        JMenuItem jmiState = new JMenuItem("State");
        JMenuItem jmiTransC = new JMenuItem("Transition cursor");
        JMenuItem jmiAddAction = new JMenuItem("Add action");

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
        jmiAbout.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: pasar el icono del bubblewizard
                JOptionPane.showMessageDialog(null, ABOUT_MSG, "Credits", JOptionPane.INFORMATION_MESSAGE, null);
            }
        });

        jmHelp.add(jmiAbout);
    }


    //TODO: listener de menu bar

}

