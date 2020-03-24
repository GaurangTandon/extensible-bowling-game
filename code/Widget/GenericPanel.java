package Widget;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

abstract class GenericPanel {

    final JPanel panel;

    GenericPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
    }

    GenericPanel(String heading) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    GenericPanel(int rows, int cols, String heading) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    Component getPanel() {
        return panel;
    }
}
