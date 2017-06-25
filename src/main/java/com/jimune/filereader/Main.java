package com.jimune.filereader;

import javax.swing.*;
import java.io.File;
import java.util.Set;

/**
 * Created by Jim on 25 Jun 17.
 */
public class Main {

    private Set<File> filesToProcess;

    public Main() {
        fileSelectionAndProcess();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void fileSelectionAndProcess() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Kies een map");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();

            if (!f.isDirectory()) {
                System.out.println("Selected file is not a directory");
                new Popup(Popup.PopupType.MESSAGEBOX, "Invalid operation", "Er is geen map geselecteerd.").show();
            } else {
                System.out.println("Folder selected: " + f.getAbsolutePath());

                int totalFiles = countFiles(f);
            }
        } else {
            System.out.println("No folder selected");
            new Popup(Popup.PopupType.MESSAGEBOX, "Invalid operation", "Er is geen map geselecteerd.").show();
        }
    }

    private int countFiles(File f) {
        int i = 0;

        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                i += countFiles(file);
            }
        } else {
            if (f.getAbsolutePath().endsWith(".csv")) i++;
        }

        return i;
    }
}
