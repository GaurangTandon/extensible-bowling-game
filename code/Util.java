import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Vector;

/**
 * Utility class that provides commonly used functionality
 * such as busyWait using a Thread, or getting the current
 * time stamp string.
 * Intended to replace equivalent pieces of code with
 * just one-line function calls of Util.
 */
class Util {
    static void busyWait(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            System.err.println("Interrupted");
        }
    }

    static String getDateString() {
        // DONE: obsolete API used, replace
        final Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + cal.get(Calendar.MONTH) +
                "/" + cal.get(Calendar.DAY_OF_WEEK) + "/" + (cal.get(Calendar.YEAR));
    }

    static boolean containsString(final Vector<String> container, final String target) {
        for (final String str : container) {
            if (str.equals(target))
                return true;
        }
        return false;
    }

    static JButton addButtonPanel(final String text, JPanel toPanel, ActionListener listener) {
        final JButton btn = new JButton(text);
        final JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        btn.addActionListener(listener);
        panel.add(btn);
        toPanel.add(panel);
        return btn;
    }

    static JTextField addFieldPanel(final String text, JPanel toPanel) {
        final JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());
        final JLabel itemLabel = new JLabel(text);
        final JTextField itemField = new JTextField("", 15);
        itemPanel.add(itemLabel);
        itemPanel.add(itemField);
        toPanel.add(itemPanel);
        return itemField;
    }
}
