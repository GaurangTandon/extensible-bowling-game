package Widget;

import javax.swing.*;
import java.awt.*;

public class WindowFrame {

    private final JFrame win;

    private WindowFrame(final String title, final Component container) {
        win = new JFrame(title);
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        win.getContentPane().add("Center", container);
        win.pack();

        // Center WindowFrame on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        final Dimension size = win.getSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((size.width) / 2),
                ((screenSize.height) / 2) - ((size.height) / 2));
        win.setVisible(true);
    }

    public WindowFrame(final String title, final GenericPanel panel) {
        this(title, panel.getPanel());
    }

    public void setVisible(final boolean state) {
        win.setVisible(state);
    }
}
