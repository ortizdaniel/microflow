package org.daniel.microflow.controller;

import org.daniel.microflow.model.Action;
import org.daniel.microflow.model.EdgeType;
import org.daniel.microflow.model.NodeType;
import org.daniel.microflow.view.ToolBar;

import javax.swing.*;
import java.awt.*;

public enum CursorDetail {

    SELECTING(Cursor.getDefaultCursor(), null, null), //normal cursor
    DELETING(createCursor(ToolBar.DELETE_ICON), null, null),
    DELETE_POPUP(Cursor.getDefaultCursor(), null, null),
    SHOW_EDIT_FUNCTION(Cursor.getDefaultCursor(), null, null),
    EDIT(Cursor.getDefaultCursor(), null, null),

    ADD_TAD(createCursor(ToolBar.TAD_ICON_C), NodeType.TAD, "Name"),
    ADD_VARIABLE(createCursor(ToolBar.VAR_ICON_C), NodeType.VARIABLE, "Variable"),
    ADD_PERIPHERAL(createCursor(ToolBar.PERIPHERAL_ICON_C), NodeType.PERIPHERAL, "Peripheral"),
    ADD_STATE(createCursor(ToolBar.STATE_ICON_C), NodeType.STATE, null),

    ADD_TRANSITION(createCursor(ToolBar.TRANSITION_CURSOR_C), EdgeType.TRANSITION, "Condition"),
    ADD_INTERRUPT(createCursor(ToolBar.INTERRUPT_ICON_C), EdgeType.INTERRUPT, "IRQ"),
    ADD_OPERATION(createCursor(ToolBar.OPERATION_ICON_C), EdgeType.OPERATION, null),
    ADD_INTERFACE(createCursor(ToolBar.INTERFACE_ICON_C), EdgeType.INTERFACE, null),
    ADD_ACTION(createCursor(ToolBar.ACTION_CURSOR_C), Action.class, "Action"),

    ADD_TEXT(createCursor(ToolBar.TEXT_ICON), NodeType.TEXT, "Default Text"),

    NEW_FILE(Cursor.getDefaultCursor(), null, null),
    OPEN_FILE(Cursor.getDefaultCursor(), null, null),
    SAVE_FILE(Cursor.getDefaultCursor(), null, null),
    SAVE_FILE_PNG(Cursor.getDefaultCursor(), null, null),
    PRINT_FILE(Cursor.getDefaultCursor(), null, null),
    GEN_FILES(Cursor.getDefaultCursor(), null, null),
    GEN_MOTOR(Cursor.getDefaultCursor(), null, null),
    GEN_DICT(Cursor.getDefaultCursor(), null, null),
    UNDO(Cursor.getDefaultCursor(), null, null),
    REDO(Cursor.getDefaultCursor(), null, null);

    private Cursor cursor;
    private Object elementToAdd;
    private String nameToAdd;

    CursorDetail(Cursor cursor, Object elementToAdd, String nameToAdd) {
        this.cursor = cursor;
        this.elementToAdd = elementToAdd;
        this.nameToAdd = nameToAdd;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Object getElementToAdd() {
        return elementToAdd;
    }

    public String getNameToAdd() {
        return nameToAdd;
    }

    private static Cursor createCursor(String path) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        if (path != null) {
            Image img = new ImageIcon(CursorDetail.class.getResource(path)).getImage()
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH);

            if (img != null) return tk.createCustomCursor(img, new Point(0, 0), "");
            return Cursor.getDefaultCursor();
        } else {
            return Cursor.getDefaultCursor();
        }
    }
}
