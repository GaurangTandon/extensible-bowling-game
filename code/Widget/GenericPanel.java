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

    GenericPanel(final String heading) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        if (!heading.isEmpty()) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    GenericPanel(final int rows, final int cols, final String heading) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        if (!heading.isEmpty()) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    GenericPanel(final JPanel fPanel) {
        panel = fPanel;
    }

    GenericPanel(final JPanel fPanel, String heading) {
        this(fPanel);
        if (!heading.isEmpty()) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    public Component getPanel() {
        return panel;
    }
}
