package Widget;

import java.awt.*;
import java.util.HashMap;

abstract class FormPanel extends GenericPanel {

    HashMap<String, Component> components;

    FormPanel(final String heading) {
        super(heading);
        setupComponentStore();
    }

    FormPanel(final int rows, final int cols, final String heading) {
        super(rows, cols, heading);
        setupComponentStore();
    }

    private void setupComponentStore() {
        components = new HashMap<>();
        components.put("_panel", panel);
    }

    public Component get(final String id) {
        return components.get(id);
    }
}
