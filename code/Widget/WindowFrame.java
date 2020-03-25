package Widget;

import javax.swing.*;
import java.awt.*;

public class WindowFrame {

    private final JFrame win;

    private WindowFrame(String title, Component container, boolean center) {
        win = new JFrame(title);
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        win.getContentPane().add("Center", container);
        win.pack();

        // Center WindowFrame on Screen
        final Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        if (center) {
            win.setLocation(
                    ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                    ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        }
        win.setVisible(true);
    }

    public WindowFrame(String title, GenericPanel panel) {
        this(title, panel.getPanel(), true);
    }

    public void setVisible(boolean state) {
        win.setVisible(state);
    }
}
