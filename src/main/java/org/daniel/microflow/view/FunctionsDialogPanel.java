package org.daniel.microflow.view;

import javax.swing.*;
import java.awt.*;

public class FunctionsDialogPanel extends JPanel {

    private static final Dimension preferredSize = new Dimension(650, 600);
    private final JTextArea textArea;

    public FunctionsDialogPanel() {
        textArea = new JTextArea();
        textArea.setTabSize(2);
        BorderLayout bl = new BorderLayout();
        bl.setVgap(10);
        setLayout(bl);
        JScrollPane scroll = new JScrollPane(textArea);
        add(new JLabel("Edit the functions for the interface here:"), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(2, 1));
        JPanel southSouth = new JPanel(new GridLayout(1, 5));
        JButton defaultTimer = new JButton("Timer"),
                defaultSIO = new JButton("SIO"),
                defaultADC = new JButton("ADC"),
                addPrePost = new JButton("Pre/Post");
        JComboBox<DefaultFunction> languageBox = new JComboBox<>(DefaultFunction.values());
        south.add(new JLabel("Add default functions in your desired language:"));
        southSouth.add(languageBox);
        southSouth.add(addPrePost);
        southSouth.add(defaultTimer);
        southSouth.add(defaultSIO);
        southSouth.add(defaultADC);
        south.add(southSouth);
        add(south, BorderLayout.SOUTH);

        defaultTimer.addActionListener(e -> textArea.append(languageBox.getItemAt(languageBox.getSelectedIndex()).getTimerText()));
        defaultSIO.addActionListener(e -> textArea.append(languageBox.getItemAt(languageBox.getSelectedIndex()).getSIOText()));
        defaultADC.addActionListener(e -> textArea.append(languageBox.getItemAt(languageBox.getSelectedIndex()).getADCText()));
        addPrePost.addActionListener(e -> textArea.append(languageBox.getItemAt(languageBox.getSelectedIndex()).getBasicText()));
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public void setText(String str) {
        textArea.setText(str);
    }

    public String getText() {
        return textArea.getText();
    }
}
