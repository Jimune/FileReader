package com.jimune.filereader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Popup implements ActionListener {

    enum PopupType {
        MESSAGEBOX,
        PROGRESSBAR;
    }

    private JFrame frame = null;
    private JButton okButton = null;
    private JProgressBar progressBar = null;
    private Vector<Component> displayComponents = null;
    private JLabel progressBarLabel = null;
    private int width = 0;

    public Popup(PopupType type, String title, Object... display) {
        this.displayComponents = new Vector<Component>();
        this.frame = new JFrame(title);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setActionCommand("ok");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 0;

        switch (type) {
            case MESSAGEBOX:
                setupMessageBox(c, panel, display);
                break;
            case PROGRESSBAR:
                try {
                    int max = 0;
                    max = Integer.parseInt((String) display[0]);
                    setupProgressBar(c, panel, max);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing value as number: " + display[0] + "; NaN!");
                    new Popup(PopupType.MESSAGEBOX, "ERROR", "Error while creating progress bar!", "Terminating program...").show();
                    System.exit(0);
                }
                break;
            default:
                break;
        }

        c.fill = GridBagConstraints.PAGE_END;
        c.insets = new Insets(10, 0, 0, 0);
        c.gridy++;
        c.gridwidth = 1;
        c.gridx = 3;

        panel.add(okButton, c);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    private void setupMessageBox(GridBagConstraints c, JPanel panel, Object... display) {
        for (Object o : display) {
            if (o instanceof String) { // TODO Add other data types such as images
                String s = (String) o;
                displayComponents.add(new JLabel(s));
                if (s.length() * 8 > width) width = s.length() * 8;
            }
        }

        frame.setSize(width + 20, 16 * display.length + 96);

        for (Component comp : displayComponents) {
            panel.add(comp, c);
            c.gridy++;
        }
    }

    private void setupProgressBar(GridBagConstraints c, JPanel panel, int max) {
        okButton.setEnabled(false);
        progressBarLabel = new JLabel("Processing files...");
        panel.add(progressBarLabel, c);

        progressBar = new JProgressBar(0, max);

        c.gridy++;
        panel.add(progressBar, c);
    }

    public void updateProgressBar(String currentFile, int current) {
        if (current > progressBar.getMaximum()) {
            progressBar.setValue(progressBar.getMaximum());
        } else {
            progressBar.setValue(current);
        }

        progressBarLabel.setText(currentFile);

        if (current >= progressBar.getMaximum()) {
            okButton.setEnabled(true);

            progressBarLabel.setText("Done...");
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("ok")) {
            frame.dispose();
        }
    }

}
