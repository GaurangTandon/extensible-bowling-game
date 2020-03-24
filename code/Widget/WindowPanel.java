package Widget;

import javax.swing.*;
import java.awt.*;

public class WindowPanel {

    private final JFrame win;

    public WindowPanel(String title, Component container) {
        win = new JFrame(title);
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        win.getContentPane().add("Center", container);
        win.pack();

        // Center WindowPanel on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    public void setVisible(boolean state) {
        win.setVisible(state);
    }
}
