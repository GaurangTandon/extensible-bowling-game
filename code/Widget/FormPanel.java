package Widget;

import java.awt.*;
import java.util.HashMap;

abstract class FormPanel extends GenericPanel {

    HashMap<String, Component> components;

    FormPanel(String heading) {
        super(heading);
        setupComponentStore();
    }

    FormPanel(int rows, int cols, String heading) {
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
