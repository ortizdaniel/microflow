package org.daniel.microflow.controller;

import org.daniel.microflow.view.OuterView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OuterController implements ChangeListener {

    private OuterView view;

    public OuterController(OuterView view) {
        this.view = view;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane source = (JTabbedPane) e.getSource();
        JPanel selected = (JPanel) source.getTabComponentAt(source.getSelectedIndex());
        String name = ((JLabel) selected.getComponent(0)).getText();
        view.setTitle("Microflow - " + name);
    }
}
