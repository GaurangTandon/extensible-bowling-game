package Widget;

import javax.swing.*;
import java.awt.*;

public class ContainerPanel extends GenericPanel {

    public ContainerPanel() {
    }

    public ContainerPanel(final JPanel panel) {
        super(panel);
    }

    public ContainerPanel(final JPanel panel, String heading) {
        super(panel);
    }

    public ContainerPanel(final String heading) {
        super(heading);
    }

    public ContainerPanel(final int rows, final int cols, final String heading) {
        super(rows, cols, heading);
    }

    public ContainerPanel put(final Component subPanel) {
        panel.add(subPanel);
        return this;
    }

    public ContainerPanel put(final Component subPanel, final String constraints) {
        panel.add(subPanel, constraints);
        return this;
    }

    public ContainerPanel put(final GenericPanel subPanel) {
        panel.add(subPanel.getPanel());
        return this;
    }

    public ContainerPanel put(final GenericPanel subPanel, final String constraints) {
        panel.add(subPanel.getPanel(), constraints);
        return this;
    }

    public JPanel getPanel() {
        return panel;
    }
}
