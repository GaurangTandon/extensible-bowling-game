package Utilities;

import Bowlers.BowlerInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class that provides commonly used functionality
 * such as busyWait using a Thread, or getting the current
 * time stamp string.
 * Intended to replace equivalent pieces of code with
 * just one-line function calls of Utilities.Util.
 */
public final class Util {
    public static final String DELIMITER = ",";

    private Util() {
    }

    public static void busyWait(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            System.err.println("Interrupted");
        }
    }

    static String getDateString() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");
        return now.format(formatter);
    }

    public static String getDateIdentifier() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        return now.format(formatter);
    }

    public static String getFileName(Component parent) {
        JFileChooser fc = new JFileChooser();
        String filepath;
        fc.setCurrentDirectory(new File("./Datastore"));
        int i = fc.showOpenDialog(parent);
        if (i == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            filepath = f.getPath();
        } else {
            System.err.println("File Selected Operation returned void.");
            filepath = "";
        }
        return filepath;
    }

    public static boolean containsString(final Iterable<String> container, final String target) {
        if (target == null) return false;
        for (final String str : container) {
            if (target.equals(str))
                return true;
        }
        return false;
    }

    public static BowlerInfo getPatronDetails(final String nickName) {
        BowlerInfo patron = null;

        try {
            patron = BowlerFile.getBowlerInfo(nickName);
        } catch (final IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }
}
