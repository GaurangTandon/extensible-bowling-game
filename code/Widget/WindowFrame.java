package Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowFrame {

    private final JFrame win;

    public WindowFrame(final String title) {
        win = new JFrame(title);

        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                win.setVisible(false);
            }
        });
    }

    private WindowFrame(final String title, final Component container) {
        win = new JFrame(title);
        win.getContentPane().setLayout(new BorderLayout());
        ((JComponent) win.getContentPane()).setOpaque(false);

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

    public void destroy() {
        // https://stackoverflow.com/a/1235994/2181238
        win.dispatchEvent(new WindowEvent(win, WindowEvent.WINDOW_CLOSING));
    }

    public void setVisible(final boolean state) {
        win.setVisible(state);
    }

    public void toggleVisible() {
        win.setVisible(!win.isVisible());
    }

    public ContainerPanel getContainer() {
        return new Widget.ContainerPanel((JPanel) win.getContentPane());
    }

    public void pack() {
        win.pack();
    }
}
