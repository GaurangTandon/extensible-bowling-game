package Widget;

import javax.naming.NameNotFoundException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ContainerPanel implements GenericPanelInterface {

    private final JPanel panel;

    public ContainerPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
    }

    public ContainerPanel(String heading) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    public ContainerPanel(int rows, int cols, String heading) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
    }

    public ContainerPanel put(final Component subPanel) {
        panel.add(subPanel);
        return this;
    }

    public ContainerPanel put(final Component subPanel, final String constraints) {
        panel.add(subPanel, constraints);
        return this;
    }

    public ContainerPanel put(final GenericPanelInterface subPanel) {
        panel.add(subPanel.getPanel());
        return this;
    }

    public ContainerPanel put(final GenericPanelInterface subPanel, final String constraints) {
        panel.add(subPanel.getPanel(), constraints);
        return this;
    }

    public Component get(final String id) throws NameNotFoundException {
        throw new NameNotFoundException();
    }

    public Component getPanel() {
        return panel;
    }
}
