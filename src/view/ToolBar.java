package view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class ToolBar extends JPanel {

    private JToolBar jtbIcons;

    /* Files */
    private JButton jbNewFile;
    private JButton jbOpenFile;
    private JButton jbSaveFile;
    private JButton jbPrint;

    /* Main Options */
    private JButton jbCursor;
    private JButton jbBack;
    private JButton jbDelete;

    /* TADs diagram */
    private JButton jbTAD;
    private JButton jbVariable;
    private JButton jbPeripheral;
    private JButton jbIface;
    private JButton jbOperation;
    private JButton jbInterrupt;

    /* States diagram */
    private JButton jbState;
    private JButton jbTransition;
    private JButton jbAction;


    private static final int MIN_WIDTH = 640;
    private static final int MIN_HEIGHT = 40;
    private static final int BUTTON_SIZE = 25;

    private static final Dimension jbDimension = new Dimension(BUTTON_SIZE, BUTTON_SIZE);

    /* Icon paths */
    private static final String NEW_FILE_ICON = "rsc/img/toolbar/new_file.png";
    private static final String OPEN_FILE_ICON = "rsc/img/toolbar/open_file.png";
    private static final String SAVE_FILE_ICON = "rsc/img/toolbar/save_file.png";
    private static final String PRINT_FILE_ICON = "rsc/img/toolbar/print_file.png";



    public ToolBar() {

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        this.setPreferredSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        jtbIcons = new JToolBar("Icons ToolBar");
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

        jbNewFile = makeCustomButton("Create a new file", NEW_FILE_ICON);
        jbOpenFile = makeCustomButton("Open an existing file", OPEN_FILE_ICON);
        jbSaveFile = makeCustomButton("Save actual file", SAVE_FILE_ICON);
        jbPrint = makeCustomButton("Print actual file", PRINT_FILE_ICON);

        jpButtons.add(jbNewFile);
        jpButtons.add(jbOpenFile);
        jpButtons.add(jbSaveFile);
        jpButtons.add(jbPrint);

        jtbIcons.add(jpButtons);
    }

    private void addMainOpButtons() {
        JPanel jpButtons = new JPanel();

        jbCursor = makeCustomButton("Select cursor", null);
        jbBack = makeCustomButton("Undo last action (Ctrl + Z)", null);
        jbDelete = makeCustomButton("Delete element", null);

        jpButtons.add(jbCursor);
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
        jbOperation = makeCustomButton("Operation cursor", null);
        jbInterrupt = makeCustomButton("Interrupt cursor", null);

        jpButtons.add(jbTAD);
        jpButtons.add(jbVariable);
        jpButtons.add(jbPeripheral);
        jpButtons.add(jbIface);
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
        jb.setBorder(BorderFactory.createEmptyBorder());
        jb.addMouseListener(new MouseAdapter() {

            private final Border hovered = BorderFactory.createBevelBorder(BevelBorder.RAISED);
            private final Border notHovered = BorderFactory.createEmptyBorder();

            @Override
            public void mouseEntered(MouseEvent e) {
                jb.setBorder(hovered);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jb.setBorder(notHovered);
            }

        });

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
