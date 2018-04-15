package view;


import controller.Controller;
import controller.CursorDetail;

import javax.swing.*;
import java.awt.event.ActionListener;


public class ContextMenu extends JPopupMenu {

    private JMenuItem jmiEdit;
    private JMenuItem jmiDelete;

    public ContextMenu() {

        jmiEdit = new JMenuItem("Edit");
        jmiDelete = new JMenuItem("Delete");

        this.add(jmiEdit);
        this.add(jmiDelete);

        this.setVisible(false);
    }

    public void hideContextMenu() {
        this.setSelected(null);
        this.setVisible(false);
    }


    public void addListener(ActionListener l) {
        jmiEdit.addActionListener(l);
        jmiEdit.setActionCommand(CursorDetail.EDIT.name());
        jmiDelete.addActionListener(l);
        jmiDelete.setActionCommand(CursorDetail.DELETE_POPUP.name());
    }
}
