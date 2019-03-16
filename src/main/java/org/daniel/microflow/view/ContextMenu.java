package org.daniel.microflow.view;

import org.daniel.microflow.controller.CursorDetail;

import javax.swing.*;
import java.awt.event.ActionListener;


public class ContextMenu extends JPopupMenu {

    private JMenuItem jmiEdit;
    private JMenuItem jmiEditFunctions;
    private JMenuItem jmiDelete;

    public ContextMenu() {

        jmiEdit = new JMenuItem("Edit");
        jmiDelete = new JMenuItem("Delete");
        jmiEditFunctions = new JMenuItem("Show/edit functions");

        this.add(jmiEdit);
        this.add(jmiEditFunctions);
        this.add(jmiDelete);

        this.setVisible(false);
    }

    public void showEditButton(boolean b) {
        jmiEdit.setVisible(b);
    }

    public void showEditFunctionButton(boolean b) {
        jmiEditFunctions.setVisible(b);
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
        jmiEditFunctions.setActionCommand(CursorDetail.SHOW_EDIT_FUNCTION.name());
        jmiEditFunctions.addActionListener(l);
    }

    public void setEditString(String s) {
        jmiEdit.setText("Edit " + s);
    }
}
