package Widget;

import java.awt.*;

public class ContainerPanel extends GenericPanel {

    public ContainerPanel() {
        super();
    }

    public ContainerPanel(String heading) {
        super(heading);
    }

    public ContainerPanel(int rows, int cols, String heading) {
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

    public Component getPanel() {
        return panel;
    }
}
