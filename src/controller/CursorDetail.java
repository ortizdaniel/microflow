package controller;

import model.Action;
import model.EdgeType;
import model.NodeType;
import view.ToolBar;

import java.awt.*;

public enum CursorDetail {

    SELECTING(Cursor.getDefaultCursor(), null, null), //normal cursor
    UNDO(Cursor.getDefaultCursor(), null, null), //no es necesario TODO borrar
    DELETING(createCursor(ToolBar.DELETE_ICON), null, null),
    DELETE_POPUP(Cursor.getDefaultCursor(), null, null),
    EDIT(Cursor.getDefaultCursor(), null, null),


    ADD_TAD(createCursor(ToolBar.TAD_ICON), NodeType.TAD, "Name"),
    ADD_VARIABLE(createCursor(ToolBar.VAR_ICON), NodeType.VARIABLE, "Variable"),
    ADD_PERIPHERAL(createCursor(ToolBar.PERIPHERAL_ICON), NodeType.PERIPHERAL, "Peripheral"),
    ADD_STATE(createCursor(ToolBar.STATE_ICON), NodeType.STATE, null),

    ADD_TRANSITION(createCursor(ToolBar.TRANSITION_CURSOR), EdgeType.TRANSITION, "Condition"),
    ADD_INTERRUPT(createCursor(ToolBar.INTERRUPT_ICON), EdgeType.INTERRUPT, "IRQ"),
    ADD_OPERATION(createCursor(ToolBar.OPERATION_ICON), EdgeType.OPERATION, null),
    ADD_INTERFACE(createCursor(ToolBar.INTERFACE_ICON), EdgeType.INTERFACE, null),
    //ADD_ACTION(createCursor(ToolBar.ACTION_CURSOR), EdgeType.ACTION, null), //deprecado
    ADD_ACTION(createCursor(ToolBar.ACTION_CURSOR), Action.class, "action 1;\naction 2;"),

    NEW_FILE(Cursor.getDefaultCursor(), null, null),
    OPEN_FILE(Cursor.getDefaultCursor(), null, null),
    SAVE_FILE(Cursor.getDefaultCursor(), null, null),
    SAVE_FILE_PNG(Cursor.getDefaultCursor(), null, null),
    PRINT_FILE(Cursor.getDefaultCursor(), null, null),
    GEN_FILES(Cursor.getDefaultCursor(), null, null),
    GEN_MOTOR(Cursor.getDefaultCursor(), null, null);

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
        if (path != null)
            return tk.createCustomCursor(tk.getImage(path), new Point(0, 0), "");
        else
            return Cursor.getDefaultCursor();
    }
}
