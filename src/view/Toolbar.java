package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;


public class Toolbar extends JPanel {

    private JToolBar jtbIcons;

    /* Files */
    private JButton jbNewFile;
    private JButton jbOpenFile;
    private JButton jbSaveFile;
    private JButton jbPrint;

    /* Main Options */
    private JButton jbBack;
    private JButton jbDelete;

    /* TADs diagram */
    private JButton jbTAD;
    private JButton jbVariable;
    private JButton jbPeripheral;
    private JButton jbIface;
    private JButton jbCall;
    private JButton jbOperation;
    private JButton jbInterrupt;

    /* States diagram */
    private JButton jbState;
    private JButton jbTransition;
    private JButton jbAction;


    private static final int MIN_WIDTH = 640;
    private static final int MIN_HEIGHT = 45;
    private static final int BUTTON_SIZE = 30;

    private static final Dimension jbDimension = new Dimension(BUTTON_SIZE, BUTTON_SIZE);

    private static int aux = 0;

    public Toolbar() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        this.setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        jtbIcons = new JToolBar("Icons Toolbar");
        jtbIcons.setFloatable(false);
        jtbIcons.setRollover(true);

        /* Add all buttons with separators */
        this.addFilesButtons();
        jtbIcons.addSeparator();
        this.addMainOpButtons();
        jtbIcons.addSeparator();
        this.addTADButtons();
        jtbIcons.addSeparator();
        this.addStatesButtons();

        this.add(jtbIcons);

    }

    private void addFilesButtons() {
        JPanel jpButtons = new JPanel();

        jbNewFile = makeCustomButton("Create a new file", null);
        jbOpenFile = makeCustomButton("Open an existing file", null);
        jbSaveFile = makeCustomButton("Save actual file", null);
        jbPrint = makeCustomButton("Print actual file", null);

        jpButtons.add(jbNewFile);
        jpButtons.add(jbOpenFile);
        jpButtons.add(jbSaveFile);
        jpButtons.add(jbPrint);

        jtbIcons.add(jpButtons);
    }

    private void addMainOpButtons() {
        JPanel jpButtons = new JPanel();

        jbBack = makeCustomButton("Undo last action (Ctrl + Z)", null);
        jbDelete = makeCustomButton("Delete element", null);

        jpButtons.add(jbBack);
        jpButtons.add(jbDelete);

        jtbIcons.add(jpButtons);
    }

    private void addTADButtons() {
        JPanel jpButtons = new JPanel();

        jbTAD = makeCustomButton("Create TAD", null);
        jbVariable = makeCustomButton("Create variable", null);
        jbPeripheral = makeCustomButton("Create peripheral", null);
        jbIface = makeCustomButton("Interface cursor", null);
        jbCall = makeCustomButton("Call cursor", null);
        jbOperation = makeCustomButton("Operation cursor", null);
        jbInterrupt = makeCustomButton("Interrupt cursor", null);

        jpButtons.add(jbTAD);
        jpButtons.add(jbVariable);
        jpButtons.add(jbPeripheral);
        jpButtons.add(jbIface);
        jpButtons.add(jbCall);
        jpButtons.add(jbOperation);
        jpButtons.add(jbInterrupt);

        jtbIcons.add(jpButtons);
    }

    private void addStatesButtons() {
        JPanel jpButtons = new JPanel();

        jbState = makeCustomButton("Create state", null);
        jbTransition = makeCustomButton("Transition cursor", null);
        jbAction = makeCustomButton("Add action", null);

        jpButtons.add(jbState);
        jpButtons.add(jbTransition);
        jpButtons.add(jbAction);

        jtbIcons.add(jpButtons);
    }


    private JButton makeCustomButton(String tipText, String iconPath) {
        JButton jb = new JButton();

        jb.setPreferredSize(jbDimension);
        jb.setToolTipText(tipText);

        try {
            //Icons must be the same size as the button
            ImageIcon icon = new ImageIcon(iconPath);
            jb.setIcon(icon);
        } catch (Exception e) {
            //TODO default image
            e.printStackTrace();
        }

        return jb;
    }

}
